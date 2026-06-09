package org.bplte.core.api.domain.comment.enums;

/**
 * 댓글 반응 요청 타입.
 *
 * <p>{@code LIKE}, {@code DISLIKE}는 반응 등록 또는 반대 반응으로의 변경을 의미하고,
 * {@code *_CANCEL}은 기존 동일 반응의 취소를 의미한다.</p>
 */
public enum CommentReactionType {
	/** 좋아요 등록 또는 싫어요에서 좋아요로 변경 */
	LIKE,
	/** 기존 좋아요 취소 */
	LIKE_CANCEL,
	/** 싫어요 등록 또는 좋아요에서 싫어요로 변경 */
	DISLIKE,
	/** 기존 싫어요 취소 */
	DISLIKE_CANCEL;
}
