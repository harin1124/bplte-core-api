package org.bplte.core.api.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;
import org.bplte.core.api.domain.comment.service.CommentService;
import org.bplte.core.api.domain.comment.swagger.CommentApiDescription;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "댓글")
public class CommentController {
	private final CommentService commentService;

	@GetMapping
	@Operation(summary = "댓글 목록 조회", description = CommentApiDescription.GET_COMMENTS)
	public ApiResponse<List<CommentDetailResponse>> getComments(
		@Valid
		@Parameter(name = "포스트 번호")
		@RequestParam Long postNumber,
		@Parameter(name = "댓글 아이디")
		@RequestParam(required = false) Long commentId,
		@CurrentUser String currentUserId) {
		return ApiResponse.success(commentService.getComments(new CommentDetailRequest(postNumber, commentId, currentUserId)));
	}

	@PostMapping
	@Operation(summary = "댓글 작성", description = CommentApiDescription.CREATE_COMMENT)
	public ApiResponse<Void> createComment(@RequestBody CommentCreateRequest request, @CurrentUser String currentUserId) {
		request.setRequestUserId(currentUserId);
		commentService.createComment(request);
		return ApiResponse.success();
	}

	@PutMapping("/{id}")
	@Operation(summary = "댓글 수정")
	public ApiResponse<Void> updateComment(@PathVariable Long id, @ModelAttribute CommentCreateRequest request) {
		return null;
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "댓글 삭제")
	public ApiResponse<Void> deleteComment(@PathVariable Long id) {
		return null;
	}

	@PatchMapping("/{id}/reaction")
	@Operation(summary = "댓글 좋아요 또는 싫어요")
	public ApiResponse<Void> reactToComment(@PathVariable Long id) {
		return null;
	}
}
