package org.bplte.core.api.domain.post.service;

import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;

public interface PostService {
	PaginationResponse<PostListResponse> getPosts(PostListRequest request);
	int createPost(PostCreateRequest request);
}
