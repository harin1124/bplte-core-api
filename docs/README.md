# 📚 BPLTE Core API 문서

> **프로젝트**: BPLTE Core API  
> **작성일**: 2026-01-18

## 📖 문서 목록

### 🎯 개발 규칙 & 컨벤션
- **[명명 규칙 가이드](conventions/NAMING_CONVENTION.md)** - URI, Controller, Service, Mapper, DTO 명명 규칙

### 🏗️ 아키텍처
- **[아키텍처 개요](architecture/README.md)** - 시스템 개요, 요청 흐름, 계층/패키지 구조
- **데이터베이스 설계** - ERD 및 테이블 구조 (예정)
- **보안 설정 가이드** - JWT, Spring Security 설정 (예정)

### 🌐 API 문서  
- **API 엔드포인트 목록** - 전체 API 목록 및 명세 (예정)
- **요청/응답 예시** - 실제 API 사용 예시 (예정)

### 🛠️ 개발 가이드
- **개발환경 설정** - 로컬 개발환경 구축 가이드 (예정)
- **배포 가이드** - 운영 배포 절차 (예정)
- **문제해결 가이드** - 자주 발생하는 이슈 해결 (예정)

---

## 요구 사항

| 항목 | 권장 버전 |
|------|-----------|
| **Java** | **17** (LTS) |
| **Build** | Gradle 9.2+ (Wrapper 포함, `./gradlew` 사용) |
| **DB** | **MariaDB** 10.6+ (또는 MySQL 8 호환) |

- **Java 17**: 프로젝트가 `JavaLanguageVersion.of(17)` 로 설정되어 있으며, Spring Boot 4 호환을 위해 17 권장
- **Gradle**: 저장소에 포함된 Wrapper(`./gradlew`) 사용 시 별도 설치 불필요
- **MariaDB**: `mariadb-java-client` 사용

### 필수 환경 변수

실행 전 아래 변수를 설정해야 합니다.

| 변수 | 설명 |
|------|------|
| `DB_URL` | JDBC URL (예: `jdbc:mariadb://localhost:3306/bplte`) |
| `DB_USERNAME` | DB 사용자명 |
| `DB_PASSWORD` | DB 비밀번호 |
| `JWT_SECRET` | JWT 서명용 시크릿 키 |
| `JWT_ACCESS_TOKEN_EXPIRATION` | 액세스 토큰 만료 시간 (예: `3600000`) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | 리프레시 토큰 만료 시간 (예: `604800000`) |

> 💡 로컬에서 IDE Run Configuration에 위 변수를 넣어 사용할 수 있습니다.

---

## 🚀 빠른 시작

### 개발환경 설정
```bash
# 프로젝트 클론
git clone [repository-url]
cd ./bplte-core-api

# 애플리케이션 실행
./gradlew bootRun
```

---

## 📝 문서 기여

새로운 문서를 추가하거나 기존 문서를 수정하실 때는 다음 구조를 따라주세요:

```
docs/
├── conventions/     # 개발 규칙 및 컨벤션
├── architecture/    # 시스템 아키텍처
├── api/             # API 관련 문서  
└── development/     # 개발 관련 가이드
```

---

> 💡 **Tip**: 문서는 실제 코드와 동기화를 유지해주세요. 코드 변경 시 관련 문서도 함께 업데이트하는 것을 권장합니다.