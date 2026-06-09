package org.bplte.core.api.domain.comment.dto.command;

import org.bplte.core.api.domain.comment.enums.CommentReactionType;

/**
 * 댓글 반응 처리 커맨드.
 *
 * @param commentId 댓글 아이디
 * @param reactionType 요청 반응 타입
 * @param requestUserId 요청 사용자 아이디
 */
public record CommentReactionCommand(
	long commentId,
	CommentReactionType reactionType,
	String requestUserId
) {}
