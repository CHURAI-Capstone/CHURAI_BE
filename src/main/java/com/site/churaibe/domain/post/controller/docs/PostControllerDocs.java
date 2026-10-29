package com.site.churaibe.domain.post.controller.docs;

import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.PostSort;
import com.site.churaibe.domain.post.enums.Spectrum;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post", description = "게시글 작성 / 조회 / 수정 / 삭제 API")
public interface PostControllerDocs {

    @Operation(summary = "게시글 작성", description = "로그인이 필요합니다. multipart/form-data로 보냅니다. title, summary(한 줄 소개), contents(조합 방법), ingredients(재료, 1~20개), category는 MAIN_DISH(식사류) 또는 DESSERT(디저트류), spectrum은 FAMILIAR/FUSION/NEW_MENU/EXPERIMENTAL이며 모두 필수입니다. coreIngredient(핵심 재료)는 비우면 첫 재료로 채워지고, tags는 최대 5개이며 선택입니다. images는 JPG/PNG/GIF/WEBP 최대 3장(장당 5MB)입니다.")
    ApiResponse<PostResDTO.PostSaveResDTO> createPost(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                      PostReqDTO.PostSaveDTO dto);

    @Operation(summary = "게시글 목록 조회 / 검색", description = "로그인 없이 조회할 수 있습니다. keyword는 제목·재료·태그에서 검색하고, category·tag·spectrum·coreIngredient로 필터링하며 조건은 모두 선택입니다(여러 개면 AND). sort는 LATEST(최신순, 기본) 또는 POPULAR(인기순, 조회수 기준)이며 page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<PostResDTO.PostPageDTO> getPosts(@Parameter(description = "검색어 (제목·재료·태그)") String keyword,
                                                 @Parameter(description = "카테고리 필터") Category category,
                                                 @Parameter(description = "태그 필터 ('#' 없이)") String tag,
                                                 @Parameter(description = "스펙트럼 필터") Spectrum spectrum,
                                                 @Parameter(description = "핵심 재료 필터") String coreIngredient,
                                                 @Parameter(description = "정렬 (LATEST | POPULAR)") PostSort sort,
                                                 @Parameter(description = "페이지 번호 (0부터)") int page,
                                                 @Parameter(description = "페이지 크기 (1~50)") int size);

    @Operation(summary = "게시글 상세 조회", description = "로그인 없이 조회할 수 있고, 조회할 때마다 조회수가 1 증가합니다. images에 이미지 id와 URL이 순서대로 담기고, reactions(반응별 개수와 내가 선택한 반응)와 scrap(스크랩 수와 내 스크랩 여부)은 로그인한 경우에 내 상태까지 반영됩니다(Authorization 헤더는 선택).")
    ApiResponse<PostResDTO.PostDetailDTO> getPost(@Parameter(hidden = true) CustomUserDetails userDetails, Long postId);

    @Operation(summary = "게시글 수정", description = "작성자 본인만 수정할 수 있습니다(아니면 403). multipart/form-data로 이미지를 제외한 모든 필드를 작성할 때와 같이 보내야 합니다. 지울 이미지는 deleteImageIds로, 새 이미지는 images로 보내며 남는 이미지와 합쳐 최대 3장입니다.")
    ApiResponse<PostResDTO.PostDetailDTO> updatePost(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                     Long postId,
                                                     PostReqDTO.PostUpdateDTO dto);

    @Operation(summary = "게시글 삭제", description = "작성자 본인만 삭제할 수 있습니다(아니면 403). 첨부된 이미지도 S3에서 함께 삭제됩니다.")
    ApiResponse<Void> deletePost(@Parameter(hidden = true) CustomUserDetails userDetails, Long postId);
}
