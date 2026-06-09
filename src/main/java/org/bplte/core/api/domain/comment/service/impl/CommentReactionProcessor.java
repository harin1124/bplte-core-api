package org.bplte.core.api.domain.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.comment.dto.command.CommentReactionCommand;
import org.bplte.core.api.domain.comment.entity.CommentReactionEntity;
import org.bplte.core.api.domain.comment.enums.CommentReactionType;
import org.bplte.core.api.domain.comment.mapper.CommentMapper;
import org.bplte.core.api.domain.comment.mapper.CommentReactionMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 댓글 반응 상태 전환을 처리한다.
 *
 * <p>사용자별 댓글 반응은 하나의 행으로 관리한다. 같은 반응 재요청은 실패시키고,
 * 좋아요/싫어요 변경은 기존 행의 {@code REACTION_TYPE}을 갱신하면서
 * {@code TBL_COMMENT}의 {@code LIKE_CNT}, {@code DISLIKE_CNT}를 함께 보정한다.</p>
 */
@Component
@RequiredArgsConstructor
public class CommentReactionProcessor {
	private final CommentMapper commentMapper;
	private final CommentReactionMapper commentReactionMapper;

	/**
	 * 댓글 반응 요청을 처리한다.
	 *
	 * @param command 댓글 반응 처리 커맨드
	 */
	public void process(CommentReactionCommand command) {
		CommentReactionEntity reactionEntity = findCommentReaction(command);

		switch(command.reactionType()) {
			case LIKE -> saveReaction(command, reactionEntity, CommentReactionType.LIKE);
			case LIKE_CANCEL -> cancelReaction(command, reactionEntity, CommentReactionType.LIKE);
			case DISLIKE -> saveReaction(command, reactionEntity, CommentReactionType.DISLIKE);
			case DISLIKE_CANCEL -> cancelReaction(command, reactionEntity, CommentReactionType.DISLIKE);
		}
	}

	/**
	 * 댓글 반응을 등록하거나 반대 반응으로 변경한다.
	 *
	 * @param command 반응 요청 커맨드
	 * @param reactionEntity 기존 반응 엔티티
	 * @param reactionType 등록 또는 변경할 반응 타입
	 */
	private void saveReaction(
		CommentReactionCommand command,
		CommentReactionEntity reactionEntity,
		CommentReactionType reactionType
	) {
		if(isSameReaction(reactionEntity, reactionType)) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		CommentReactionEntity entity = createReactionEntity(command, reactionType);
		if(reactionEntity == null) {
			commentReactionMapper.insertCommentReaction(entity);
		} else {
			entity.setReactionId(reactionEntity.getReactionId());
			commentReactionMapper.updateCommentReaction(entity);
			updateCommentReactionCount(command.commentId(), CommentReactionType.valueOf(reactionEntity.getReactionType()), -1);
		}

		updateCommentReactionCount(command.commentId(), reactionType, 1);
	}

	/**
	 * 기존 댓글 반응을 취소한다.
	 *
	 * @param command 반응 요청 커맨드
	 * @param reactionEntity 기존 반응 엔티티
	 * @param reactionType 취소할 반응 타입
	 */
	private void cancelReaction(
		CommentReactionCommand command,
		CommentReactionEntity reactionEntity,
		CommentReactionType reactionType
	) {
		if(!isSameReaction(reactionEntity, reactionType)) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		CommentReactionEntity entity = createReactionEntity(command, reactionType);
		entity.setReactionId(reactionEntity.getReactionId());
		commentReactionMapper.deleteCommentReaction(entity);
		updateCommentReactionCount(command.commentId(), reactionType, -1);
	}

	/**
	 * 요청 사용자의 기존 댓글 반응을 조회한다.
	 *
	 * @param command 반응 요청 커맨드
	 * @return 기존 댓글 반응. 없으면 {@code null}
	 */
	private CommentReactionEntity findCommentReaction(CommentReactionCommand command) {
		CommentReactionEntity reactionParam = CommentReactionEntity.builder()
			.commentId(command.commentId())
			.userId(command.requestUserId())
			.build();
		List<CommentReactionEntity> list = commentReactionMapper.selectCommentReactionEntityList(reactionParam);
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * 반응 처리용 엔티티를 생성한다.
	 *
	 * @param command 반응 요청 커맨드
	 * @param reactionType 처리할 반응 타입
	 * @return 댓글 반응 엔티티
	 */
	private CommentReactionEntity createReactionEntity(
		CommentReactionCommand command,
		CommentReactionType reactionType
	) {
		return CommentReactionEntity.builder()
			.commentId(command.commentId())
			.userId(command.requestUserId())
			.reactionType(reactionType.name())
			.rgtrId(command.requestUserId())
			.mdfrId(command.requestUserId())
			.build();
	}

	/**
	 * 기존 반응이 요청한 반응 타입과 같은지 확인한다.
	 *
	 * @param reactionEntity 기존 반응 엔티티
	 * @param reactionType 비교할 반응 타입
	 * @return 같은 반응이면 {@code true}
	 */
	private boolean isSameReaction(CommentReactionEntity reactionEntity, CommentReactionType reactionType) {
		return reactionEntity != null && reactionType.name().equals(reactionEntity.getReactionType());
	}

	/**
	 * 댓글의 좋아요/싫어요 수 캐시를 증감한다.
	 *
	 * @param commentId 댓글 아이디
	 * @param reactionType 증감할 반응 타입
	 * @param countDelta 증감 값
	 */
	private void updateCommentReactionCount(long commentId, CommentReactionType reactionType, int countDelta) {
		commentMapper.updateCommentReactionCount(commentId, reactionType.name(), countDelta);
	}
}
