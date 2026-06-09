package org.bplte.core.api.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.post.dto.request.MyPostListRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.response.PostDetailResponse;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.entity.PostEntity;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {
	int insertPost(PostEntity post);
	int deletePost(PostEntity post);
	int updatePost(PostEntity post);
	int selectPostListCount(PostListRequest param);
	int updateInquiryCountUp(Long postNumber);
	List<PostListResponse> selectPostList(PostListRequest param);
	List<PostListResponse> selectMyPostList(MyPostListRequest param);
	PostDetailResponse selectPost(Long postNumber);
	Optional<PostEntity> selectPostByPostNumber(Long postNumber);
}
