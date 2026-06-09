package org.bplte.core.api.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;
import org.bplte.core.api.domain.comment.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {
	Optional<CommentEntity> selectCommentByCommentId(long commentId);
	List<CommentDetailResponse> selectCommentList(CommentDetailRequest request);
	void insertComment(CommentEntity comment);
	void updateComment(CommentEntity comment);
	void deleteComment(CommentEntity comment);

	/**
	 * 댓글의 좋아요/싫어요 수 캐시를 증감한다.
	 *
	 * @param commentId 댓글 아이디
	 * @param reactionType 증감할 반응 타입
	 * @param countDelta 증감 값
	 */
	void updateCommentReactionCount(
		@Param("commentId") long commentId,
		@Param("reactionType") String reactionType,
		@Param("countDelta") int countDelta
	);
}
