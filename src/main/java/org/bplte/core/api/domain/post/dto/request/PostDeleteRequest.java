package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [요청] 포스트 삭제
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 포스트 삭제 (PostDeleteRequest)")
public class PostDeleteRequest {
	@Schema(description = "포스트 번호 (고유값)")
	private Long postNumber;
	@Schema(description = "수정자 아이디", hidden = true)
	private String mdfrId;
}
