package org.bplte.core.api.domain.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.file.dto.request.DeleteFileListInput;
import org.bplte.core.api.domain.file.dto.request.FileListRequest;
import org.bplte.core.api.domain.file.dto.request.SaveFileListInput;
import org.bplte.core.api.domain.file.dto.response.PostFileListResponse;
import org.bplte.core.api.domain.file.entity.FileEntity;
import org.bplte.core.api.domain.file.enums.FileRefType;
import org.bplte.core.api.domain.file.enums.FileRoleType;
import org.bplte.core.api.domain.file.service.FileService;
import org.bplte.core.api.domain.post.dto.request.*;
import org.bplte.core.api.domain.post.dto.response.PostDetailResponse;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.entity.PostEntity;
import org.bplte.core.api.domain.post.enums.PostSortColumn;
import org.bplte.core.api.domain.post.mapper.PostMapper;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostMapper postMapper;
	private final FileService fileService;

	@Override
	public PaginationResponse<PostListResponse> getPosts(PostListRequest request) {
		int totalCount = postMapper.selectPostListCount(request);
		List<PostListResponse> postList = new ArrayList<>(0);
		
		if(totalCount > 0) {
			request.setSortColumnName(PostSortColumn.toDbColumn(request.getSortColumnName()));
			postList = postMapper.selectPostList(request);
		}
		
		return PaginationResponse.of(request.getSize(), totalCount, postList);
	}

	@Override
	public PostDetailResponse getPost(Long postNumber) {
		return Optional.ofNullable(postMapper.selectPost(postNumber))
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));
	}

	@Override
	public List<PostFileListResponse> getPostFiles(Long postNumber) {
		FileListRequest request = new FileListRequest();
		request.setRefId(postNumber.toString());
		request.setRefType(FileRefType.POST_ATTACHMENT);
		List<FileEntity> fileEntityList = fileService.selectFileList(request);

		List<PostFileListResponse> fileList = new ArrayList<>(fileEntityList.size());
		for(FileEntity entity : fileEntityList) {
			PostFileListResponse response = new PostFileListResponse();
			response.setFileId(entity.getFileId());
			response.setFileName(entity.getOriginalName() + "." + entity.getExtension());
			response.setFileSize(entity.getFileSize());
			response.setSortOrder(entity.getSortOrder());
			fileList.add(response);
		}

		return fileList;
	}

	@Override
	public List<PostListResponse> getMyPosts(MyPostListRequest request) {
		return postMapper.selectMyPostList(request);
	}

	@Override
	public void updatePostViewCountUp(Long postNumber) {
		int result = postMapper.updateInquiryCountUp(postNumber);
		if(result == 0) {
			throw new ApiException(ResponseCodeGeneral.NOT_FOUND);
		}
	}

	@Override
	@Transactional
	public int createPost(PostCreateRequest request) {
		PostEntity postEntity = PostEntity.createToEntity(request);
		int result = postMapper.insertPost(postEntity);

		if(request.getAttachFileList() != null && !request.getAttachFileList().isEmpty()) {
			SaveFileListInput param = new SaveFileListInput();
			param.setFileList(request.getAttachFileList());
			param.setFileNameOrderList(request.getAttachFileOrderList());
			param.setRefId(postEntity.getPostNumber().toString());
			param.setRgtrId(request.getRequestUserId());
			param.setRefType(FileRefType.POST_ATTACHMENT);
			param.setRoleType(FileRoleType.ORIGINAL);

			fileService.saveFileList(param);
		}

		return result;
	}

	@Override
	public int deletePost(PostDeleteRequest request) {
		PostEntity postInfo = postMapper.selectPostByPostNumber(request.getPostNumber())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));
		
		if(!postInfo.getOwnerUserId().equals(request.getMdfrId())){
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}
		
		return postMapper.deletePost(PostEntity.deleteToEntity(request));
	}

	@Override
	@Transactional
	public int updatePost(PostUpdateRequest request) {
		PostEntity postInfo = postMapper.selectPostByPostNumber(request.getPostNumber())
			.orElseThrow(() -> new ApiException(ResponseCodeGeneral.NOT_FOUND));
		
		if(!postInfo.getOwnerUserId().equals(request.getMdfrId())) {
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}

		// 삭제 파일 처리
		if(request.getDeleteFileIdList() != null && !request.getDeleteFileIdList().isEmpty()) {
			DeleteFileListInput deleteParam = DeleteFileListInput.builder()
				.refType(FileRefType.POST_ATTACHMENT)
				.refId(request.getPostNumber().toString())
				.fileIdList(request.getDeleteFileIdList())
				.mdfrId(request.getMdfrId())
				.build();
			fileService.deleteFileList(deleteParam);
		}

		// 신규 등록 파일 처리 (최종 순서는 attachFileOrderList 기반 재정렬에서 반영)
		boolean hasNewAttach = request.getAddAttachFileList() != null && !request.getAddAttachFileList().isEmpty();
		if (hasNewAttach) {
			SaveFileListInput saveParam = new SaveFileListInput();
			saveParam.setFileList(request.getAddAttachFileList());
			saveParam.setFileNameOrderList(null);
			saveParam.setRefId(request.getPostNumber().toString());
			saveParam.setRgtrId(request.getMdfrId());
			saveParam.setRefType(FileRefType.POST_ATTACHMENT);
			saveParam.setRoleType(FileRoleType.ORIGINAL);
			fileService.saveFileList(saveParam);
		}

		boolean hasAttachOrder = request.getAttachFileOrderList() != null && !request.getAttachFileOrderList().isEmpty();
		if (hasAttachOrder) {
			fileService.reorderFilesByOriginalName(
				FileRefType.POST_ATTACHMENT,
				request.getPostNumber().toString(),
				FileRoleType.ORIGINAL,
				request.getAttachFileOrderList(),
				request.getMdfrId());
		}

		return postMapper.updatePost(PostEntity.updateToEntity(request));
	}
}
