package com.site.churaibe.domain.reaction.dto.request;

import com.site.churaibe.domain.reaction.enums.ReactionType;
import jakarta.validation.constraints.NotNull;

public class ReactionReqDTO {

    public record ReactionDTO(
            @NotNull(message = "반응 종류는 필수입니다.")
            ReactionType type
    ) {
    }
}
