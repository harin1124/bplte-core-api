package org.bplte.core.api.domain.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;
import org.bplte.core.api.domain.post.dto.request.PostDeleteRequest;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.request.PostUpdateRequest;
import org.bplte.core.api.domain.post.dto.response.PostDetailResponse;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.entity.PostEntity;
import org.bplte.core.api.domain.post.mapper.PostMapper;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public PostDetailResponse getPost(Long postNumber) {
		return Optional.ofNullable(postMapper.selectPost(postNumber))
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));
	}
	
	public void updatePostViewCountUp(Long postNumber) {
		int result = postMapper.updateInquiryCountUp(postNumber);
		if(result == 0) {
			throw new ApiException(ResponseCodeGeneral.NOT_FOUND);
		}
	}
	
	public int createPost(PostCreateRequest request) {
		return postMapper.insertPost(PostEntity.createToEntity(request));
	}

	public int deletePost(PostDeleteRequest request) {
		PostEntity postInfo = postMapper.selectPostByPostNumber(request.getPostNumber());
		
		if(postInfo == null) {
			throw new ApiException(ResponseCodeGeneral.NOT_FOUND);
		}
		
		if(!postInfo.getOwnerUserId().equals(request.getMdfrId())){
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}
		
		return postMapper.deletePost(PostEntity.deleteToEntity(request));
	}

	public int updatePost(PostUpdateRequest request) {
		PostEntity postInfo = postMapper.selectPostByPostNumber(request.getPostNumber());
		
		if(postInfo == null) {
			throw new ApiException(ResponseCodeGeneral.NOT_FOUND);
		}
		
		if(!postInfo.getOwnerUserId().equals(request.getMdfrId())){
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}
		
		return postMapper.updatePost(PostEntity.updateToEntity(request));
	}
}
