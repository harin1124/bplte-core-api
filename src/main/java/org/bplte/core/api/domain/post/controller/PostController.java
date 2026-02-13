package org.bplte.core.api.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "포스트 (PostController)")
public class PostController {
	private final PostService postService;
	
	@GetMapping
	@Operation(summary = "포스트 목록 조회")
	public ApiResponse<PaginationResponse<PostListResponse>> getPosts(@ModelAttribute @Valid PostListRequest request) {
		return ApiResponse.success(postService.getPosts(request));
	}
	
	@PostMapping
	@Operation(summary = "포스트 등록")
	public ApiResponse<Integer> createPost(
			@RequestBody @Valid PostCreateRequest request,
			@CurrentUser String userId) {
		request.setRequestUserId(userId);
		return ApiResponse.success(postService.createPost(request));
	}
}
