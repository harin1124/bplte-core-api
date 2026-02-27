package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [요청] 포스트 수정
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 포스트 수정 (PostUpdateRequest)")
public class PostUpdateRequest {
	@Schema(description = "포스트 번호", hidden = true)
	private Long postNumber;
	@NotBlank
	@Size(min = 1, max = 100)
	@Schema(description = "제목", minLength = 1, maxLength = 100)
	private String title;
	@NotBlank
	@Schema(description = "내용", minLength = 1)
	private String content;
	@Schema(description = "검색 내용", hidden = true)
	private String searchContent;
	@Schema(description = "수정자 아이디", hidden = true)
	private String mdfrId;
}
