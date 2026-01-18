package org.bplte.core.api.domain.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.entity.PostEntity;
import org.bplte.core.api.domain.post.mapper.PostMapper;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostMapper postMapper;
	
	public List<PostListResponse> getPosts() {
		return postMapper.selectPostList();
	}
	
	public Integer createPost(PostCreateRequest request) {
		return postMapper.insertPost(PostEntity.toEntity(request));
	}
}
