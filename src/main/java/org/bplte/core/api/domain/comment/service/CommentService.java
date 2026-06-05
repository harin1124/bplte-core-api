package org.bplte.core.api.domain.comment.service;

import org.bplte.core.api.domain.comment.dto.request.CommentCreateRequest;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;

import java.util.List;

public interface CommentService {
	List<CommentDetailResponse> getComments(CommentDetailRequest request);
	void createComment(CommentCreateRequest request);
}
