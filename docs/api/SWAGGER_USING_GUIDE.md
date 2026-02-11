# Swagger 사용 가이드

> BPLTE Core API의 OpenAPI(Swagger) 문서를 이용해 API를 확인하고 테스트하는 방법입니다.

---

## 1. Swagger 문서 경로

애플리케이션 실행 후 아래 주소로 접속합니다.

| 용도                         | 경로                            |
|------------------------------|---------------------------------|
| **Swagger UI** (문서 + 실행) | `/bplte/core/swagger-ui.html`   |
| **OpenAPI JSON** (명세 원본) | `/bplte/core/v3/api-docs`       |

- Swagger UI: 브라우저에서 API 목록을 보고 **Try it out**으로 요청을 보낼 수 있습니다.
- OpenAPI JSON: 다른 도구(Postman, 코드 생성 등)에서 사용할 수 있는 OpenAPI 3.0 명세입니다.

---

## 2. 인증 없이 사용하는 API vs 인증이 필요한 API

### 인증 없이 사용하는 API

다음 API는 **로그인 없이** Swagger UI에서 바로 호출할 수 있습니다.

- **Auth(인증)** 태그
  - `POST /auth/join` — 회원가입
  - `POST /auth/login` — 로그인 (여기서 발급받은 토큰으로 아래 인증 API 사용)

이 외에 **Swagger UI**, **v3/api-docs** 경로 자체도 인증 없이 접근 가능합니다.

### 인증이 필요한 API

**Auth 태그를 제외한 나머지 API**는 모두 **JWT 인증**이 필요합니다.

- 예: **Post** 태그의 `GET /posts`, `POST /posts` 등

문서에서 자물쇠(🔒) 아이콘이 붙은 API는 인증 후에만 호출해야 하며, 인증하지 않으면 `401 Unauthorized`가 반환됩니다.

---

## 3. Swagger UI에서 인증하는 방법

인증이 필요한 API를 Swagger UI에서 호출하려면, 먼저 로그인해서 **JWT accessToken**을 받은 뒤 Swagger에 등록해야 합니다.

### 3.1 로그인해서 토큰 받기

1. Swagger UI에서 **Auth** 태그를 펼칩니다.
2. **POST /auth/login** 을 열고 **Try it out** 을 클릭합니다.
3. Request body에 로그인 계정 정보를 입력 후 **Execute** 를 누릅니다.
4. 응답 본문의 **accessToken** 값을 복사합니다.  
   (`"Bearer "` 접두사는 **붙이지 않고** 토큰 값만 복사합니다.)

### 3.2 Swagger에 토큰 등록하기

1. Swagger UI **우측 상단**의 **Authorize** 버튼을 클릭합니다.
2. **bearerAuth** 섹션의 **Value** 입력란에 위에서 복사한 **accessToken 값만** 붙여넣습니다.
3. **Authorize** 를 클릭한 뒤 창을 닫습니다.

이후에는 인증이 필요한 API를 호출할 때마다 자동으로  
`Authorization: Bearer <accessToken>` 헤더가 붙습니다.

**참고:** Swagger UI에서는 API 요청 시 **쿠키(Cookie)를 보내지 않도록** 설정되어 있습니다.  
프론트에서 HttpOnly 쿠키로 인증하는 경우에도, Swagger UI에서는 Authorize에 입력한 토큰만 사용하므로, **로그아웃(Authorize 해제) 후에는 인증 없이 호출 시 401**이 반환됩니다.

### 3.3 인증 해제

다시 **Authorize** 를 클릭한 뒤 **Logout** 하면 저장된 토큰이 제거됩니다.

---

## 4. 요약

| 구분            | 설  명                                                     |
|-----------------|------------------------------------------------------------|
| **Swagger UI**  | `http://localhost:8081/bplte/core/swagger-ui.html`         |
| **인증 불필요** | `/auth/join`, `/auth/login`                                |
| **인증 필요**   | 그 외 API (Auth 제외) — JWT 필요                           |
| **인증 방법**   | 로그인 → accessToken 복사 → Authorize에 토큰만 입력        |