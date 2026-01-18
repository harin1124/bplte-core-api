package org.bplte.core.api.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

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
