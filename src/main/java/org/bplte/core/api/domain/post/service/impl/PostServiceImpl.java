package org.bplte.core.api.domain.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.entity.PostEntity;
import org.bplte.core.api.domain.post.mapper.PostMapper;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostMapper postMapper;
	
	public PaginationResponse<PostListResponse> getPosts(PostListRequest request) {
		int totalCount = postMapper.selectPostListCount();
		List<PostListResponse> postList = new ArrayList<>(0);
		
		if(totalCount > 0) {
			postList = postMapper.selectPostList(request);
		}
		
		return PaginationResponse.of(request.getSize(), totalCount, postList);
	}
	
	public int createPost(PostCreateRequest request) {
		return postMapper.insertPost(PostEntity.createToEntity(request));
	}
}
