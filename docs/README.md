# 📚 BPLTE Core API 문서

> **프로젝트**: BPLTE Core API  
> **작성일**: 2026-01-18

## 📖 문서 목록

### 🎯 개발 규칙 & 컨벤션
- **[명명 규칙 가이드](conventions/NAMING_CONVENTION.md)** - URI, Controller, Service, Mapper, DTO 명명 규칙

### 🏗️ 아키텍처
- **계층 구조 설명** - Service, Mapper, DTO 계층별 역할 (예정)
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

## 🚀 빠른 시작

### 개발환경 설정
```bash
# 프로젝트 클론
git clone [repository-url]
cd bplte-core/bplte-core-api

# 애플리케이션 실행
./gradlew bootRun
```

### API 테스트
```bash
# 서버 상태 확인
curl http://localhost:8080/health

# 인증 테스트  
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"userId":"test","password":"test"}'
```

---

## 📝 문서 기여

새로운 문서를 추가하거나 기존 문서를 수정하실 때는 다음 구조를 따라주세요:

```
docs/
├── conventions/     # 개발 규칙 및 컨벤션
├── architecture/    # 시스템 아키텍처
├── api/            # API 관련 문서  
└── development/    # 개발 관련 가이드
```

---

> 💡 **Tip**: 문서는 실제 코드와 동기화를 유지해주세요. 코드 변경 시 관련 문서도 함께 업데이트하는 것을 권장합니다.