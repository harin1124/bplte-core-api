package org.bplte.core.api.domain.comment.service;

import org.bplte.core.api.domain.comment.dto.command.CommentDeleteCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentReactionCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentUpdateCommand;
import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;

import java.util.List;

public interface CommentService {
	List<CommentDetailResponse> getComments(CommentDetailRequest request);
	void createComment(CommentCreateRequest request, String currentUserId);
	void updateComment(CommentUpdateCommand command);
	void deleteComment(CommentDeleteCommand command);

	/**
	 * 댓글 좋아요/싫어요 반응을 처리한다.
	 *
	 * <p>반응 등록, 반대 반응으로 변경, 기존 반응 취소를 처리하며 댓글의 반응 수 캐시도 함께 갱신한다.</p>
	 *
	 * @param command 댓글 반응 처리 커맨드
	 */
	void reactionComment(CommentReactionCommand command);
}
