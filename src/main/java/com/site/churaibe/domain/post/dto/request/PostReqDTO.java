package com.site.churaibe.domain.post.dto.request;

import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.Spectrum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
            // 조합 방법
            String contents,

            @NotNull(message = "카테고리는 필수입니다.")
            Category category,

            @NotBlank(message = "한 줄 소개는 필수입니다.")
            @Size(max = 100, message = "한 줄 소개는 100자 이하여야 합니다.")
            String summary,

            @NotEmpty(message = "재료는 1개 이상 입력해야 합니다.")
            @Size(max = 20, message = "재료는 20개 이하여야 합니다.")
            List<@NotBlank(message = "재료 이름은 비어 있을 수 없습니다.") @Size(max = 50, message = "재료 이름은 50자 이하여야 합니다.") String> ingredients,

            // 비워 두면 첫 번째 재료가 핵심 재료가 됨
            @Size(max = 50, message = "핵심 재료는 50자 이하여야 합니다.")
            String coreIngredient,

            @NotNull(message = "스펙트럼은 필수입니다.")
            Spectrum spectrum,

            // '#' 없이 보내도 되고, 최대 5개
            @Size(max = 5, message = "태그는 5개 이하여야 합니다.")
            List<@NotBlank(message = "태그는 비어 있을 수 없습니다.") @Size(max = 30, message = "태그는 30자 이하여야 합니다.") String> tags,

            // 최대 3장, 없으면 이미지 없이 작성
            List<MultipartFile> images
    ) {
    }

    // 전체 수정(PUT): 이미지를 제외한 모든 필드를 보내야 함
    // 이미지는 deleteImageIds로 지울 것을 지정하고, images로 새로 추가 (남는 이미지 + 새 이미지 합쳐 최대 3장)
    public record PostUpdateDTO(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
            String title,

            @NotBlank(message = "내용은 필수입니다.")
            @Size(max = 5000, message = "내용은 5000자 이하여야 합니다.")
            // 조합 방법
            String contents,

            @NotNull(message = "카테고리는 필수입니다.")
            Category category,

            @NotBlank(message = "한 줄 소개는 필수입니다.")
            @Size(max = 100, message = "한 줄 소개는 100자 이하여야 합니다.")
            String summary,

            @NotEmpty(message = "재료는 1개 이상 입력해야 합니다.")
            @Size(max = 20, message = "재료는 20개 이하여야 합니다.")
            List<@NotBlank(message = "재료 이름은 비어 있을 수 없습니다.") @Size(max = 50, message = "재료 이름은 50자 이하여야 합니다.") String> ingredients,

            // 비워 두면 첫 번째 재료가 핵심 재료가 됨
            @Size(max = 50, message = "핵심 재료는 50자 이하여야 합니다.")
            String coreIngredient,

            @NotNull(message = "스펙트럼은 필수입니다.")
            Spectrum spectrum,

            // '#' 없이 보내도 되고, 최대 5개
            @Size(max = 5, message = "태그는 5개 이하여야 합니다.")
            List<@NotBlank(message = "태그는 비어 있을 수 없습니다.") @Size(max = 30, message = "태그는 30자 이하여야 합니다.") String> tags,

            List<Long> deleteImageIds,

            List<MultipartFile> images
    ) {
    }
}
