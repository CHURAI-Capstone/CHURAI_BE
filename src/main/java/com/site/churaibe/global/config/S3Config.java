package com.site.churaibe.global.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class S3Config {

    // 자격증명은 기본 자격증명 체인(AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY 환경변수 등)에서 요청 시점에 읽음
    @Bean
    public S3Client s3Client(@Value("${cloud.aws.region}") String region,
                             @Value("${cloud.aws.s3.endpoint}") String endpoint) {
        S3ClientBuilder builder = S3Client.builder().region(Region.of(region));

        // MinIO 같은 S3 호환 스토리지는 버킷을 경로(path)로 구분
        if (!endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint)).forcePathStyle(true);
        }
        return builder.build();
    }
}
