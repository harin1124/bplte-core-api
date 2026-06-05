package org.bplte.core.api.domain.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;
import org.bplte.core.api.domain.comment.entity.CommentEntity;
import org.bplte.core.api.domain.comment.mapper.CommentMapper;
import org.bplte.core.api.domain.comment.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentMapper commentMapper;

	@Override
	public List<CommentDetailResponse> getComments(CommentDetailRequest request) {
		return commentMapper.selectCommentList(request);
	}

	@Override
	@Transactional
	public void createComment(CommentCreateRequest request) {
		commentMapper.insertComment(CommentEntity.createToEntity(request));
	}
}
