package org.bplte.core.api.domain.comment.entity;

import lombok.*;
import org.bplte.core.api.domain.comment.dto.command.CommentDeleteCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentUpdateCommand;
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

	/**
	 * 등록용 엔티티로 변환
	 *
	 * @param request 등록 요청 값
	 * @param requestUserId 요청 사용자 아이디
	 * @return 댓글 엔티티
	 */
	public static CommentEntity createToEntity(CommentCreateRequest request, String requestUserId) {
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
			.rgtrId(requestUserId)
			.mdfrId(requestUserId)
			.build();
	}

	/**
	 * 수정용 엔티티로 변환
	 *
	 * @param command 수정 요청 커맨드
	 * @return 댓글 엔티티
	 */
	public static CommentEntity updateToEntity(CommentUpdateCommand command) {
		return CommentEntity.builder()
			.commentId(command.commentId())
			.content(command.content())
			.mdfrId(command.requestUserId())
			.build();
	}

	/**
	 * 삭제용 엔티티로 변환
	 *
	 * @param command 삭제 요청 커맨드
	 * @return 댓글 엔티티
	 */
	public static CommentEntity deleteToEntity(CommentDeleteCommand command) {
		return CommentEntity.builder()
			.commentId(command.commentId())
			.mdfrId(command.requestUserId())
			.build();
	}
}
