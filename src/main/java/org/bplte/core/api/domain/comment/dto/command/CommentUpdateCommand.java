package org.bplte.core.api.domain.comment.dto.command;

/**
 * [커맨드] 댓글 수정
 * @param commentId 댓글 아이디
 * @param content 내용
 * @param requestUserId 요청 사용자 아이디
 */
public record CommentUpdateCommand(
	long commentId,
	String content,
	String requestUserId
) {}
