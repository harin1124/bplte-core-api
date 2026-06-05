package org.bplte.core.api.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.comment.dto.request.CommentDetailRequest;
import org.bplte.core.api.domain.comment.dto.response.CommentDetailResponse;
import org.bplte.core.api.domain.comment.entity.CommentEntity;

import java.util.List;

@Mapper
public interface CommentMapper {
	List<CommentDetailResponse> selectCommentList(CommentDetailRequest request);
	void insertComment(CommentEntity comment);
}
