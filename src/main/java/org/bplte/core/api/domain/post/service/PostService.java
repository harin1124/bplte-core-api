package org.bplte.core.api.domain.post.service;

import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.domain.file.dto.response.PostFileListResponse;
import org.bplte.core.api.domain.post.dto.request.*;
import org.bplte.core.api.domain.post.dto.response.PostDetailResponse;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;

import java.util.List;

public interface PostService {
	PaginationResponse<PostListResponse> getPosts(PostListRequest request);
	PostDetailResponse getPost(Long postNumber);
	List<PostFileListResponse> getPostFiles(Long postNumber);
	List<PostListResponse> getMyPosts(MyPostListRequest request);
	void updatePostViewCountUp(Long postNumber);
	int createPost(PostCreateRequest request);
	int deletePost(PostDeleteRequest request);
	int updatePost(PostUpdateRequest request);
}
