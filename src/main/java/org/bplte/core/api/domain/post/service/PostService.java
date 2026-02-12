package org.bplte.core.api.domain.post.service;

import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;

import java.util.List;

public interface PostService {
	List<PostListResponse> getPosts(PostListRequest request);
	int createPost(PostCreateRequest request);
}
