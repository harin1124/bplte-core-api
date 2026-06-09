package org.bplte.core.api.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.comment.entity.CommentReactionEntity;

import java.util.List;

@Mapper
public interface CommentReactionMapper {
	/**
	 * 조건에 맞는 댓글 반응 목록을 조회한다.
	 *
	 * @param entity 댓글 아이디와 선택적 사용자 아이디 조회 조건
	 * @return 댓글 반응 목록
	 */
	List<CommentReactionEntity> selectCommentReactionEntityList(CommentReactionEntity entity);

	/**
	 * 댓글 반응을 등록한다.
	 *
	 * @param entity 등록할 댓글 반응 정보
	 */
	void insertCommentReaction(CommentReactionEntity entity);

	/**
	 * 기존 댓글 반응의 타입을 변경한다.
	 *
	 * @param entity 변경할 댓글 반응 정보
	 */
	void updateCommentReaction(CommentReactionEntity entity);

	/**
	 * 댓글 반응을 삭제한다.
	 *
	 * @param entity 삭제할 댓글 반응 정보
	 */
	void deleteCommentReaction(CommentReactionEntity entity);
}
