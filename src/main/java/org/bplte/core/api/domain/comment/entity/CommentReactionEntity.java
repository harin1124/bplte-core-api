package org.bplte.core.api.domain.comment.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 댓글 좋아요/싫어요
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionEntity {
	/** 반응 아이디 */
	private Long reactionId;
	/** 댓글 아이디 */
	private Long commentId;
	/** 사용자 아이디 */
	private String userId;
	/** 반응 구분 (LIKE/DISLIKE) */
	private String reactionType;
	/** 삭제 여부 (반응 취소) */
	private String delYn;
	/** 등록 일시 */
	private LocalDateTime regDt;
	/** 등록자 아이디 */
	private String rgtrId;
	/** 수정 일시 */
	private LocalDateTime mdfcnDt;
	/** 수정자 아이디 */
	private String mdfrId;
}
