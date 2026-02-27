package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(name = "[요청] 포스트 등록 (PostCreateRequest)")
public class PostCreateRequest {
	@NotBlank
	@Size(min = 1, max = 100)
	@Schema(description = "제목", minLength = 1, maxLength = 100)
	private String title;
	@NotBlank
	@Schema(description = "내용", minLength = 1)
	private String content;
	@Schema(hidden = true)
	private String searchContent;
	@Schema(hidden = true)
	private String requestUserId;
}
