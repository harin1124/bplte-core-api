package org.bplte.core.api.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	
	@GetMapping
	public ApiResponse<List<PostListResponse>> getPosts() {
		return ApiResponse.success(postService.getPosts());
	}
	
	@PostMapping
	public ApiResponse<Integer> createPost(
			@RequestBody @Valid PostCreateRequest request,
			@CurrentUser String userId) {
		request.setRequestUserId(userId);
		return ApiResponse.success(postService.createPost(request));
	}
}
