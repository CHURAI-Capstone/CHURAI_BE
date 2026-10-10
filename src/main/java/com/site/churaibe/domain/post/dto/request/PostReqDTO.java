package com.site.churaibe.domain.post.dto.request;

import com.site.churaibe.domain.post.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostReqDTO {

    public record PostSaveDTO(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            @Size(max = 5000, message = "내용은 5000자 이하여야 합니다.")
            String contents,

            @NotNull(message = "카테고리는 필수입니다.")
            Category category
    ) {
    }

    // 전체 수정(PUT): 세 필드 모두 보내야 함
    public record PostUpdateDTO(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            @Size(max = 5000, message = "내용은 5000자 이하여야 합니다.")
            String contents,

            @NotNull(message = "카테고리는 필수입니다.")
            Category category
    ) {
    }
}
