package org.bplte.core.api.domain.comment.entity;

import lombok.*;
import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;

import java.time.LocalDateTime;

/**
 * 댓글
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
	/** 댓글 아이디 */
	private Long commentId;
	/** 포스트 번호 */
	private Long postNumber;
	/** 상위 댓글 아이디 */
	private Long parentCommentId;
	/** 루트 댓글 아이디 */
	private Long rootCommentId;
	/** 댓글 깊이 */
	private int depth;
	/** 내용 */
	private String content;
	/** 좋아요 수 */
	private int likeCnt;
	/** 싫어요 수 */
	private int dislikeCnt;
	/** 비밀 여부 */
	private String secretYn;
	/** 삭제 여부 */
	private String delYn;
	/** 등록 일시 */
	private LocalDateTime regDt;
	/** 등록자 아이디 */
	private String rgtrId;
	/** 수정 일시 */
	private LocalDateTime mdfcnDt;
	/** 수정자 아이디 */
	private String mdfrId;

	public static CommentEntity createToEntity(CommentCreateRequest request) {
		return CommentEntity.builder()
			.postNumber(request.getPostNumber())
			.parentCommentId(request.getParentCommentId())
			.rootCommentId(request.getRootCommentId())
			.depth(request.getDepth())
			.content(request.getContent())
			.likeCnt(0)
			.dislikeCnt(0)
			.secretYn(request.getSecretYn())
			.delYn("N")
			.rgtrId(request.getRequestUserId())
			.mdfrId(request.getRequestUserId())
			.build();
	}
}
