# 보안 설정 가이드 (Security Configuration Guide)

> **프로젝트**: BPLTE Core API  
> **작성일**: 2026-03-03  
> **목적**: JWT 인증 및 Spring Security 설정 이해 및 운영 시 참고

---

## 1. 개요

- **인증 방식**: Stateless JWT (세션 없음)
- **토큰 전달**: `Authorization: Bearer <token>` 또는 HttpOnly 쿠키(`accessToken`)
- **인증 실패 시**: 401 Unauthorized + JSON 응답

---

## 2. JWT 설정

### 2.1 환경 변수 / 프로퍼티

| 프로퍼티 | 환경 변수 | 설명 |
|----------|-----------|------|
| `jwt.secret` | `JWT_SECRET` | 서명용 시크릿 키 (HMAC-SHA 사용, **최소 32바이트 권장**) |
| `jwt.access-token-expiration` | `JWT_ACCESS_TOKEN_EXPIRATION` | 액세스 토큰 유효 시간 (밀리초, 예: `3600000` = 1시간) |
| `jwt.refresh-token-expiration` | `JWT_REFRESH_TOKEN_EXPIRATION` | 리프레시 토큰 유효 시간 (밀리초, 예: `604800000` = 7일) |

설정 클래스: `JwtProperties` (`@ConfigurationProperties(prefix = "jwt")`)

### 2.2 토큰 생성·검증 (JwtTokenProvider)

- **Access Token**: `subject` = 사용자 ID, `roles` 클레임 = 권한 목록
- **Refresh Token**: `subject` = 사용자 ID (roles 없음)
- **알고리즘**: HMAC-SHA (jjwt 0.12.x, `SecretKey` 기반)
- **검증 실패 시**: 만료/형식 오류 등은 로그만 남기고 인증 실패로 처리

---

## 3. Spring Security 설정

### 3.1 SecurityFilterChain 요약

| 항목 | 설정 |
|------|------|
| **CSRF** | 비활성화 (Stateless API) |
| **세션** | `SessionCreationPolicy.STATELESS` |
| **CORS** | 허용 Origin 패턴 `*`, GET/POST/PUT/DELETE/OPTIONS, 모든 헤더, `allowCredentials(true)` |
| **예외 처리** | 인증 실패 시 `JwtAuthenticationEntryPoint` → 401 JSON |

### 3.2 URL별 접근 제어

| 경로 | 접근 |
|------|------|
| `/auth/**` | 인증 없이 허용 (회원가입, 로그인 등) |
| `/swagger-ui/**`, `/swagger-ui.html`, `/v3/api-docs/**` | 인증 없이 허용 |
| **그 외** | 인증 필요 (`authenticated()`) |

### 3.3 필터 순서

1. **AuthAvailableUserIdRateLimitFilter** (Order 1)  
   - `GET /auth/available/user_id` 에 대해 IP 단위 Rate Limit (토큰 버킷, Bucket4j)  
   - local 프로필에서는 비활성화  
2. **JwtAuthenticationFilter** (Order 2)  
   - 요청에서 토큰 추출 → 검증 → `SecurityContext`에 인증 정보 설정  

---

## 4. 토큰 추출 방식 (JwtAuthenticationFilter)

1. **HttpOnly 쿠키**  
   - 쿠키 이름: `accessToken` (`AuthProperties.accessToken`)  
   - 같은 도메인에서 브라우저가 자동 전송  
2. **Authorization 헤더**  
   - `Authorization: Bearer <accessToken>`  
   - Swagger UI, 모바일 앱 등에서 사용  

---

## 5. 인증 실패 처리 (JwtAuthenticationEntryPoint)

- 인증되지 않은 사용자가 보호된 URL에 접근 시 호출
- HTTP 401, `Content-Type: application/json`
- 응답 body: `ApiResponse` 형식 (예: `ResponseCodeGeneral.UNAUTHORIZED`)

---

## 6. Rate Limit (AuthAvailableUserIdRateLimitFilter)

- **대상**: `GET /auth/available/user_id` (아이디 중복 확인 등)
- **방식**: IP 단위 토큰 버킷 (10회 / 10분), Caffeine 캐시로 IP별 버킷 관리
- **초과 시**: HTTP 429, `Retry-After` 헤더, JSON 에러 응답
- **local 프로필**: 적용 안 함

---

## 7. Swagger UI와 쿠키 (SwaggerUiNoCredentialsFilter)

- Swagger UI 페이지에서 `fetch` 호출 시 `credentials: 'omit'` 주입
- HttpOnly 쿠키를 쓰는 환경에서 “Authorize 해제 후에도 쿠키가 전송되는” 문제 방지
- Swagger에서 인증 테스트 시에는 **Authorize**에 Bearer 토큰을 직접 넣어 사용

---

## 8. CORS

- `CorsConfigurationSource` 빈에서 전역 설정
- 허용: 모든 Origin 패턴, GET/POST/PUT/DELETE/OPTIONS, 모든 헤더, `allowCredentials(true)`
- 운영 환경에서는 필요 시 `addAllowedOriginPattern`을 도메인으로 제한하는 것을 권장

---

## 9. 운영 시 권장 사항

| 항목 | 권장 |
|------|------|
| **JWT_SECRET** | 32바이트 이상, 예측 불가한 값, 코드/저장소에 포함 금지 |
| **토큰 만료** | Access Token은 짧게(예: 1시간), Refresh Token은 제한적으로 길게 |
| **CORS** | 프로덕션에서는 허용 Origin을 필요한 도메인만 지정 |
| **Rate Limit** | 필요 시 로그인/회원가입 등 다른 경로에도 확장 검토 |

---

## 10. 관련 클래스 위치

| 역할 | 클래스 |
|------|--------|
| Security 설정 | `config/SecurityConfig.java` |
| JWT 설정 프로퍼티 | `config/jwt/JwtProperties.java` |
| JWT 생성·검증 | `config/jwt/JwtTokenProvider.java` |
| JWT 인증 필터 | `config/filter/JwtAuthenticationFilter.java` |
| 인증 실패 처리 | `config/filter/JwtAuthenticationEntryPoint.java` |
| Rate Limit 필터 | `config/filter/AuthAvailableUserIdRateLimitFilter.java` |
| Swagger 쿠키 제어 | `config/filter/SwaggerUiNoCredentialsFilter.java` |
| 쿠키 이름 상수 | `domain/auth/property/AuthProperties.java` |