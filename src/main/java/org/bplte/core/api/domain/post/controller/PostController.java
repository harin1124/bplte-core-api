package org.bplte.core.api.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.file.dto.response.PostFileListResponse;
import org.bplte.core.api.domain.post.dto.request.*;
import org.bplte.core.api.domain.post.dto.response.PostDetailResponse;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "포스트")
public class PostController {
	private final PostService postService;
	
	@GetMapping
	@Operation(summary = "포스트 목록 조회")
	public ApiResponse<PaginationResponse<PostListResponse>> getPosts(@ModelAttribute @Valid PostListRequest request) {
		return ApiResponse.success(postService.getPosts(request));
	}
	
	@GetMapping("/me")
	@Operation(summary = "내가 작성한 포스트 목록 조회")
	public ApiResponse<List<PostListResponse>> getMyPosts(
			@RequestParam String userId,
			@CurrentUser String currentUserId) {
		if(!userId.equals(currentUserId)) {
			throw new ApiException(ResponseCodeGeneral.FORBIDDEN);
		}
		return ApiResponse.success(postService.getMyPosts(new MyPostListRequest(currentUserId)));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "포스트 상세 조회")
	public ApiResponse<PostDetailResponse> getPost(@PathVariable Long id) {
		return ApiResponse.success(postService.getPost(id));
	}

	@GetMapping("/{id}/files")
	@Operation(summary = "포스트 상세 첨부파일 조회")
	public ApiResponse<List<PostFileListResponse>> getPostFiles(@PathVariable Long id) {
		return ApiResponse.success(postService.getPostFiles(id));
	}
	
	@PatchMapping("/{id}/view_count")
	@Operation(summary = "포스트 조회 수 증가")
	public ApiResponse<Void> updatePostViewCountUp(@PathVariable Long id) {
		postService.updatePostViewCountUp(id);
		return ApiResponse.success();
	}
	
	@PostMapping
	@Operation(summary = "포스트 등록")
	public ApiResponse<Integer> createPost(
			@ModelAttribute @Valid PostCreateRequest request,
			HttpServletRequest httpRequest,
			@CurrentUser String userId) {
		request.setRequestUserId(userId);
		applyAttachFileOrderListFromRawRequest(request, httpRequest);
		return ApiResponse.success(postService.createPost(request));
	}

	/**
	 * multipart에서 {@code attachFileOrderList[n]} 형태는 {@code @ModelAttribute}만으로 비는 경우가 있어,
	 * 서블릿 파라미터 맵에서 직접 읽어 덮어쓴다.
	 */
	private static void applyAttachFileOrderListFromRawRequest(PostCreateRequest request, HttpServletRequest http) {
		List<String> indexed = parseIndexedFormFieldTexts(http, "attachFileOrderList");
		if (!indexed.isEmpty()) {
			request.setAttachFileOrderList(indexed);
			return;
		}
		String[] flat = http.getParameterValues("attachFileOrderList");
		if (flat == null) {
			return;
		}
		List<String> list = Arrays.stream(flat)
			.filter(v -> v != null && !v.isBlank())
			.toList();
		if (!list.isEmpty()) {
			request.setAttachFileOrderList(new ArrayList<>(list));
		}
	}

	private static List<String> parseIndexedFormFieldTexts(HttpServletRequest http, String baseName) {
		TreeMap<Integer, String> sorted = new TreeMap<>();
		for (Map.Entry<String, String[]> e : http.getParameterMap().entrySet()) {
			String key = e.getKey();
			if (!key.startsWith(baseName) || key.length() <= baseName.length()) {
				continue;
			}
			if (key.charAt(baseName.length()) != '[' || !key.endsWith("]")) {
				continue;
			}
			String indexPart = key.substring(baseName.length() + 1, key.length() - 1);
			int idx;
			try {
				idx = Integer.parseInt(indexPart);
			} catch (NumberFormatException ex) {
				continue;
			}
			String[] vals = e.getValue();
			if (vals == null || vals.length == 0) {
				continue;
			}
			String v = vals[0];
			if (v == null || v.isBlank()) {
				continue;
			}
			sorted.put(idx, v);
		}
		return new ArrayList<>(sorted.values());
	}

	@PutMapping("/{id}")
	@Operation(summary = "포스트 수정")
	public ApiResponse<Integer> updatePost(
			@PathVariable Long id,
			@RequestBody @Valid PostUpdateRequest request,
			@CurrentUser String userId) {
		request.setPostNumber(id);
		request.setMdfrId(userId);
		return ApiResponse.success(postService.updatePost(request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "포스트 삭제")
	public ApiResponse<Integer> deletePost(@PathVariable Long id, @CurrentUser String userId) {
		return ApiResponse.success(postService.deletePost(new PostDeleteRequest(id, userId)));
	}
}
