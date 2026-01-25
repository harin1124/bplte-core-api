package org.bplte.core.api.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 로그인 정보 (AuthLoginResponse)")
public class AuthLoginResponse {
	/** 사용자 아이디 */
	@Schema(description = "사용자 아이디")
	private String userId;
	/** 사용자 이름 */
	@Schema(description = "사용자 이름")
	private String userName;
	/** 접근 토큰 */
	@Schema(description = "접근 토큰")
	private String accessToken;
}
