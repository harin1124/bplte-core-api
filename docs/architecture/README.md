# BPLTE Core API 아키텍처

---

## 1. 시스템 개요

```
┌─────────────┐      HTTP/JSON       ┌──────────────────────────────────┐      JDBC       ┌─────────────┐
│   Client    │ ◄──────────────────► │      BPLTE Core API (Spring)     │ ◄─────────────► │  MariaDB    │
│ (Web/App)   │   /bplte/core/...    │  Security · JWT · MVC · MyBatis  │                 │             │
└─────────────┘                      └──────────────────────────────────┘                 └─────────────┘
```

- **역할**: REST API 서버. 인증(JWT), 도메인 비즈니스(회원/포스트 등), DB 접근(MyBatis)을 담당
- **Context Path**: `/bplte/core`
- **인증**: Stateless JWT. 세션 없이 `Authorization: Bearer <token>` 또는 Cookie 기반으로 인증

---

## 2. 기술 스택 요약

| 구분 | 기술 |
|------|------|
| **런타임** | Java 17 |
| **프레임워크** | Spring Boot 4.0, Spring Security, Spring Web MVC |
| **ORM/DB 접근** | MyBatis 3.x, MariaDB (JDBC) |
| **인증** | JWT (jjwt 0.12.x), Stateless |
| **로깅** | Log4j2 (log4jdbc로 SQL 로깅) |
| **API 문서** | SpringDoc OpenAPI 3 (Swagger UI) |
| **기타** | Lombok, Bean Validation |