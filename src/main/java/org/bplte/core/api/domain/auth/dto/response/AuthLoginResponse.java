package org.bplte.core.api.domain.auth.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponse {
	/** 사용자 아이디 */
	private String userId;
	/** 사용자 이름 */
	private String userName;
	/** 접근 토큰 */
	private String accessToken;
}
