package org.bplte.core.api.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [요청] 포스트 등록
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
	@NotBlank
	private String title;
	private String content;
	private String searchContent;
	private String requestUserId;
}
