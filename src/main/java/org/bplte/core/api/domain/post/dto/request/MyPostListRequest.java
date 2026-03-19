package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [요청] 나의 포스트 조회
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPostListRequest {
	@Schema(hidden = true)
	private String userId;
}
