package org.bplte.core.api.domain.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.comment.dto.command.CommentDeleteCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentReactionCommand;
import org.bplte.core.api.domain.comment.dto.command.CommentUpdateCommand;
import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;
import org.bplte.core.api.domain.comment.entity.CommentEntity;
import org.bplte.core.api.domain.comment.mapper.CommentMapper;
import org.bplte.core.api.domain.comment.service.CommentService;
import org.bplte.core.api.domain.post.entity.PostEntity;
import org.bplte.core.api.domain.post.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentMapper commentMapper;
	private final CommentReactionProcessor commentReactionProcessor;
	private final PostMapper postMapper;

	@Override
	public List<CommentDetailResponse> getComments(CommentDetailRequest request) {
		return commentMapper.selectCommentList(request);
	}

	@Override
	@Transactional
	public void createComment(CommentCreateRequest request, String currentUserId) {
		commentMapper.insertComment(CommentEntity.createToEntity(request, currentUserId));
	}

	@Override
	@Transactional
	public void updateComment(CommentUpdateCommand command) {
		// 댓글 정보 가져오기 및 존재 여부 확인
		CommentEntity entity = commentMapper.selectCommentByCommentId(command.commentId())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));

		// 삭제된 댓글인 경우, 수정 불가
		if(entity.getDelYn().equals("Y")) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		// 댓글 등록자와 요청 사용자 아이디가 다를 경우(본인이 아닌 경우), 수정 불가
		if(!entity.getRgtrId().equals(command.requestUserId())) {
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}

		// 존재하지 않는 게시글이거나 삭제된 경우, 수정 불가
		PostEntity postEntity = postMapper.selectPostByPostNumber(entity.getPostNumber())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.BAD_REQUEST));
		if(postEntity.getDelYn().equals("Y")) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		commentMapper.updateComment(CommentEntity.updateToEntity(command));
	}

	@Override
	@Transactional
	public void deleteComment(CommentDeleteCommand command) {
		// 댓글 정보 가져오기 및 존재 여부 확인
		CommentEntity entity = commentMapper.selectCommentByCommentId(command.commentId())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));

		// 댓글 등록자와 요청 사용자 아이디가 다를 경우(본인이 아닌 경우), 삭제 불가
		if(!entity.getRgtrId().equals(command.requestUserId())) {
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}

		// 존재하지 않는 게시글인 경우, 삭제 불가
		postMapper.selectPostByPostNumber(entity.getPostNumber())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.BAD_REQUEST));

		commentMapper.deleteComment(CommentEntity.deleteToEntity(command));
	}

	@Override
	@Transactional
	public void reactionComment(CommentReactionCommand command) {
		CommentEntity entity = commentMapper.selectCommentByCommentId(command.commentId())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));

		// 삭제된 댓글인 경우, 반응 불가
		if(entity.getDelYn().equals("Y")) {
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}

		commentReactionProcessor.process(command);
	}
}
