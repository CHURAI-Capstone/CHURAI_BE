package com.site.churaibe.global.s3;

import com.site.churaibe.global.apiPayload.code.S3ErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class S3Service {
    private static final String KEY_PREFIX = "posts/";
    // 허용하는 이미지 형식 (확장자는 파일명이 아니라 Content-Type 기준으로 정함)
    private static final Map<String, String> IMAGE_EXTENSIONS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/gif", "gif",
            "image/webp", "webp"
    );

    private final S3Client s3Client;
    private final String bucket;
    private final String region;
    private final String endpoint;

    public S3Service(S3Client s3Client,
                     @Value("${cloud.aws.s3.bucket}") String bucket,
                     @Value("${cloud.aws.region}") String region,
                     @Value("${cloud.aws.s3.endpoint}") String endpoint) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.region = region;
        this.endpoint = endpoint;
    }

    public record UploadedFile(String key, String url) {
    }

    public static boolean isSupportedImage(MultipartFile file) {
        return file.getContentType() != null && IMAGE_EXTENSIONS.containsKey(file.getContentType());
    }

    /**
     * 파일들을 S3에 올린다. 중간에 실패하면 이미 올라간 파일을 지우고 예외를 던진다.
     * 호출한 트랜잭션이 롤백되면 올라간 파일도 함께 지운다.
     */
    public List<UploadedFile> upload(List<MultipartFile> files) {
        List<UploadedFile> uploaded = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                uploaded.add(put(file));
            }
        } catch (RuntimeException e) {
            deleteQuietly(uploaded.stream().map(UploadedFile::key).toList());
            throw e;
        }

        runOnRollback(() -> deleteQuietly(uploaded.stream().map(UploadedFile::key).toList()));
        return uploaded;
    }

    /** 트랜잭션이 커밋된 뒤에 S3 객체를 지운다 (롤백되면 DB에 남은 이미지가 깨지지 않도록). */
    public void deleteAfterCommit(Collection<String> keys) {
        if (keys.isEmpty()) {
            return;
        }
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            deleteQuietly(keys);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deleteQuietly(keys);
            }
        });
    }

    private UploadedFile put(MultipartFile file) {
        if (bucket.isBlank()) {
            log.error("S3 bucket is not configured (AWS_S3_BUCKET_NAME)");
            throw new GeneralException(S3ErrorCode.S3_UPLOAD_FAILED);
        }

        String key = KEY_PREFIX + UUID.randomUUID() + "." + IMAGE_EXTENSIONS.get(file.getContentType());
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try {
            // 스트림은 한 번 읽으면 재시도할 수 없어서, 5MB 이하 이미지는 바이트로 올림
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException | RuntimeException e) {
            log.error("S3 upload failed. key={}", key, e);
            throw new GeneralException(S3ErrorCode.S3_UPLOAD_FAILED);
        }
        return new UploadedFile(key, urlOf(key));
    }

    private void deleteQuietly(Collection<String> keys) {
        if (keys.isEmpty() || bucket.isBlank()) {
            return;
        }
        try {
            List<ObjectIdentifier> objects = keys.stream()
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .toList();
            s3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(objects).build())
                    .build());
        } catch (RuntimeException e) {
            // 이미 DB 처리는 끝났으므로 사용자 요청은 실패시키지 않고 로그만 남김
            log.error("S3 delete failed. keys={}", keys, e);
        }
    }

    private void runOnRollback(Runnable action) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    action.run();
                }
            }
        });
    }

    private String urlOf(String key) {
        if (!endpoint.isBlank()) {
            return endpoint + "/" + bucket + "/" + key;
        }
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }
}
