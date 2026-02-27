package org.bplte.core.api.domain.post.service;

import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.request.PostDeleteRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.request.PostUpdateRequest;
import org.bplte.core.api.domain.post.dto.response.PostDetailResponse;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;

public interface PostService {
	PaginationResponse<PostListResponse> getPosts(PostListRequest request);
	PostDetailResponse getPost(Long postNumber);
	int createPost(PostCreateRequest request);
	int deletePost(PostDeleteRequest request);
	int updatePost(PostUpdateRequest request);
}
