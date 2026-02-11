package org.bplte.core.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI(Swagger) 문서 설정.
 * Bearer JWT 인증 스킴을 등록하고 전역 보안 요구사항을 적용하여
 * Swagger UI에 "Authorize" 버튼이 보이도록 한다.
 * <p>
 * 사용 방법:
 * 1. /auth/login 으로 로그인 후 응답의 accessToken 값을 복사
 * 2. Swagger UI 우측 상단 "Authorize" 클릭
 * 3. Value 에 accessToken 만 입력 후 Authorize
 * 4. 이후 /posts 등 인증 필요 API 호출 시 자동으로 Authorization: Bearer &lt;token&gt; 이 붙음
 */
@Configuration
public class OpenApiConfig {

	/** SecurityScheme 이름. Authorize 시 이 이름으로 등록된 스킴이 사용된다. */
	public static final String SECURITY_SCHEME_BEARER = "bearerAuth";

	@Bean
	public OpenAPI openAPI() {
		// 1) Bearer JWT 스킴 정의 → Swagger UI에 "Authorize" 입력란 생성
		SecurityScheme bearerScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.description("로그인(/auth/login) 응답의 accessToken 값을 입력하세요. 'Bearer ' 접두사는 입력하지 않아도 됩니다.");

		Components components = new Components()
			.addSecuritySchemes(SECURITY_SCHEME_BEARER, bearerScheme);

		// 2) 전역 보안 요구사항 → 모든 API에 이 스킴이 필요하다고 문서에 표시 (자물쇠 아이콘)
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList(SECURITY_SCHEME_BEARER);

		return new OpenAPI()
			.components(components)
			.addSecurityItem(securityRequirement);
	}
}
