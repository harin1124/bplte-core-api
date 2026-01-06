package org.bplte.core.api.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	/** 시크릿 키 - 최소 32바이트(256비트) 이상 필요 */
	private String secret;
	/** accessToken 유효 시간 (밀리초) => 24시간 */
	private long accessTokenExpiration;
	/** refreshToken 유효 시간 (밀리초) => 7일 */
	private long refreshTokenExpiration;
}