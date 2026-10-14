package com.site.churaibe.domain.post.dto.request;

import com.site.churaibe.domain.post.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class PostReqDTO {

    public record PostSaveDTO(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            @Size(max = 5000, message = "내용은 5000자 이하여야 합니다.")
            String contents,

            @NotNull(message = "카테고리는 필수입니다.")
            Category category,

            // 최대 3장, 없으면 이미지 없이 작성
            List<MultipartFile> images
    ) {
    }

    // 전체 수정(PUT): title, contents, category는 모두 보내야 함
    // 이미지는 deleteImageIds로 지울 것을 지정하고, images로 새로 추가 (남는 이미지 + 새 이미지 합쳐 최대 3장)
    public record PostUpdateDTO(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            @Size(max = 5000, message = "내용은 5000자 이하여야 합니다.")
            String contents,

            @NotNull(message = "카테고리는 필수입니다.")
            Category category,

            List<Long> deleteImageIds,

            List<MultipartFile> images
    ) {
    }
}
