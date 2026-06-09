package org.bplte.core.api.domain.comment.dto.command;

/**
 * [커맨드] 댓글 삭제
 * @param commentId 댓글 아이디
 * @param requestUserId 요청 사용자 아이디
 */
public record CommentDeleteCommand(
	long commentId,
	String requestUserId
) {}
