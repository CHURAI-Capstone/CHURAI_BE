package com.site.churaibe.domain.post.controller.docs;

import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post", description = "게시글 작성 / 조회 / 수정 / 삭제 API")
public interface PostControllerDocs {

    @Operation(summary = "게시글 작성", description = "로그인이 필요합니다. multipart/form-data로 보냅니다. category는 MAIN_DISH(식사류) 또는 DESSERT(디저트류)이고, images는 JPG/PNG/GIF/WEBP 최대 3장(장당 5MB)입니다.")
    ApiResponse<PostResDTO.PostSaveResDTO> createPost(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                      PostReqDTO.PostSaveDTO dto);

    @Operation(summary = "게시글 목록 조회", description = "로그인 없이 조회할 수 있습니다. 최신순이며 page는 0부터 시작, size는 1~50입니다.")
    ApiResponse<PostResDTO.PostPageDTO> getPosts(@Parameter(description = "페이지 번호 (0부터)") int page,
                                                 @Parameter(description = "페이지 크기 (1~50)") int size);

    @Operation(summary = "게시글 상세 조회", description = "로그인 없이 조회할 수 있고, 조회할 때마다 조회수가 1 증가합니다. images에 이미지 id와 URL이 순서대로 담깁니다.")
    ApiResponse<PostResDTO.PostDetailDTO> getPost(Long postId);

    @Operation(summary = "게시글 수정", description = "작성자 본인만 수정할 수 있습니다(아니면 403). multipart/form-data로 title, contents, category를 모두 보내야 합니다. 지울 이미지는 deleteImageIds로, 새 이미지는 images로 보내며 남는 이미지와 합쳐 최대 3장입니다.")
    ApiResponse<PostResDTO.PostDetailDTO> updatePost(@Parameter(hidden = true) CustomUserDetails userDetails,
                                                     Long postId,
                                                     PostReqDTO.PostUpdateDTO dto);

    @Operation(summary = "게시글 삭제", description = "작성자 본인만 삭제할 수 있습니다(아니면 403). 첨부된 이미지도 S3에서 함께 삭제됩니다.")
    ApiResponse<Void> deletePost(@Parameter(hidden = true) CustomUserDetails userDetails, Long postId);
}
