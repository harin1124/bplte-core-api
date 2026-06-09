package org.bplte.core.api.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.domain.comment.dto.command.CommentDeleteCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentReactionCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentUpdateCommand;
import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentUpdateRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;
import org.bplte.core.api.domain.comment.enums.CommentReactionType;
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
		@Parameter(name = "포스트 번호") @RequestParam Long postNumber,
		@Parameter(name = "댓글 아이디") @RequestParam(required = false) Long commentId,
		@CurrentUser String currentUserId
	) {
		return ApiResponse.success(commentService.getComments(new CommentDetailRequest(postNumber, commentId, currentUserId)));
	}

	@PostMapping
	@Operation(summary = "댓글 작성", description = CommentApiDescription.CREATE_COMMENT)
	public ApiResponse<Void> createComment(@RequestBody CommentCreateRequest request, @CurrentUser String currentUserId) {
		commentService.createComment(request, currentUserId);
		return ApiResponse.success();
	}

	@PutMapping("/{id}")
	@Operation(summary = "댓글 수정", description = CommentApiDescription.UPDATE_COMMENT)
	public ApiResponse<Void> updateComment(
		@Schema(description = "댓글 아이디")
		@PathVariable long id,

		@RequestBody CommentUpdateRequest request,
		@CurrentUser String currentUserId
	) {
		commentService.updateComment(new CommentUpdateCommand(id, request.content(), currentUserId));
		return ApiResponse.success();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "댓글 삭제", description = CommentApiDescription.DELETE_COMMENT)
	public ApiResponse<Void> deleteComment(@Schema(description = "댓글 아이디") @PathVariable long id, @CurrentUser String currentUserId) {
		commentService.deleteComment(new CommentDeleteCommand(id, currentUserId));
		return ApiResponse.success();
	}

	@PatchMapping("/{id}/reaction")
	@Operation(summary = "댓글 반응", description = CommentApiDescription.REACTION_COMMENT)
	public ApiResponse<Void> reactionComment(
		@Schema(description = "댓글 아이디")
		@PathVariable long id,

		@Schema(description = "반응 타입(좋아요, 좋아요 취소, 싫어요, 싫어요 취소")
		@RequestParam CommentReactionType reactionType,

		@CurrentUser String currentUserId
	) {
		commentService.reactionComment(new CommentReactionCommand(id, reactionType, currentUserId));
		return ApiResponse.success();
	}
}
