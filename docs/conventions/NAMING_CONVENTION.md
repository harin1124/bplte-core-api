# 📋 명명 규칙 가이드 (Naming Convention Guide)

> **프로젝트**: BPLTE Core API  
> **작성일**: 2026-01-18  
> **목적**: 계층별 일관된 명명 규칙을 통한 코드 가독성 및 유지보수성 향상

---

## 🎯 기본 원칙

### 1. **계층별 관점 차이 인정**
- **Service Layer**: 비즈니스 관점 (도메인 친화적)
- **Mapper Layer**: 데이터베이스 관점 (SQL 동작 기반)
- **DTO Layer**: 화면/API 관점 (데이터 구조 중심)

### 2. **명명 패턴**
- **메서드**: `동사 + 명사` (행위 중심)
- **클래스**: `명사 + 동사/형용사` (데이터 구조 중심)

---

## 🌐 1. URI 패턴 (RESTful API)

### 기본 구조
```
/{리소스명}
/{리소스명}/{id}
/{리소스명}/search
```

### CRUD 패턴
| 동작 | HTTP Method | URI 패턴 | 예시 |
|------|-------------|----------|------|
| **목록 조회** | `GET` | `/posts` | 게시글 목록 |
| **단건 조회** | `GET` | `/posts/{id}` | 특정 게시글 |
| **검색** | `GET` | `/posts/search?keyword=value` | 게시글 검색 |
| **생성** | `POST` | `/posts` | 게시글 생성 |
| **전체 수정** | `PUT` | `/posts/{id}` | 게시글 전체 수정 |
| **부분 수정** | `PATCH` | `/posts/{id}` | 게시글 부분 수정 |
| **삭제** | `DELETE` | `/posts/{id}` | 게시글 삭제 |

### 조건부 조회
```
GET /posts?userId={id}          # 사용자별 게시글
GET /posts?status=published     # 발행된 게시글
GET /posts?page=1&size=20       # 페이징
```

---

## 📱 2. Controller Layer

### 메서드 명명 규칙: `HTTP메서드 기반 + 도메인명`

```java
@RestController
@RequestMapping("/posts")
public class PostController {
    
    // 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts();
    
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id);
    
    @GetMapping("/search")  
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String keyword);
    
    // 생성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest request);
    
    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostUpdateRequest request);
    
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> patchPost(@PathVariable Long id, @RequestBody PostPatchRequest request);
    
    // 삭제  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id);
}
```

### 패턴
| HTTP Method | Controller Method | 설명 |
|-------------|-------------------|------|
| `GET` | `getXxx()` | 단건/목록 조회 |
| `GET` | `searchXxx()` | 검색 기능 |
| `POST` | `createXxx()` | 데이터 생성 |
| `PUT` | `updateXxx()` | 전체 데이터 수정 |
| `PATCH` | `patchXxx()` | 부분 데이터 수정 |
| `DELETE` | `deleteXxx()` | 데이터 삭제 |

---

## 🏢 3. Service Layer

### 메서드 명명 규칙: `비즈니스 동사 + 도메인명`

```java
@Service
public class PostService {
    
    // 조회 (비즈니스 관점)
    public List<PostResponse> getPostList();                    // 목록 조회
    public PostResponse getPost(Long id);                       // 단건 조회  
    public PostResponse getPostDetail(Long id);                 // 상세 조회
    public List<PostResponse> getPostsByUser(Long userId);      // 조건부 조회
    public List<PostResponse> searchPosts(String keyword);      // 검색
    
    // 생성
    public PostResponse createPost(PostCreateRequest request);  // 생성 (반환값 있음)
    public void addPost(PostCreateRequest request);             // 생성 (반환값 없음)
    
    // 수정  
    public PostResponse updatePost(Long id, PostUpdateRequest request);  // 수정
    public void modifyPost(Long id, PostUpdateRequest request);          // 수정 (반환값 없음)
    
    // 삭제
    public void deletePost(Long id);                            // 삭제
    public void removePost(Long id);                            // 제거 (논리삭제 시)
    
    // 비즈니스 로직
    public void publishPost(Long id);                           // 발행
    public void hidePost(Long id);                              // 숨기기
}
```

### 동사 선택 가이드
| 동작 | 주요 동사 | 대안 동사 | 사용 상황 |
|------|-----------|-----------|-----------|
| **조회** | `get` | `find`, `search` | get: 일반조회, find: 조건조회, search: 검색 |
| **생성** | `create` | `add`, `register` | create: 일반생성, register: 등록 |
| **수정** | `update` | `modify`, `change` | update: 전체수정, modify: 부분수정 |
| **삭제** | `delete` | `remove` | delete: 물리삭제, remove: 논리삭제 |

---

## 🗄️ 4. Mapper Layer

### 메서드 명명 규칙: `SQL동사 + 도메인명`

```java
@Mapper
public interface PostMapper {
    
    // 조회 (SQL 관점)
    List<PostEntity> selectPostList();                          // 목록 조회
    PostEntity selectPostById(Long id);                         // ID로 조회
    List<PostEntity> selectPostsByUserId(Long userId);          // 조건부 조회
    List<PostEntity> selectPostsByKeyword(String keyword);      // 키워드 검색
    
    // 집계/검증
    int countPosts();                                            // 전체 개수
    int countPostsByStatus(String status);                       // 조건부 개수  
    boolean existsPostById(Long id);                             // 존재 여부
    
    // 생성
    int insertPost(PostEntity post);                             // 단건 입력
    void insertPostBatch(List<PostEntity> posts);               // 배치 입력
    
    // 수정
    int updatePost(PostEntity post);                             // 전체 수정
    int updatePostStatus(Long id, String status);               // 부분 수정
    int updatePostViewCount(Long id);                            // 조회수 증가
    
    // 삭제  
    int deletePost(Long id);                                     // 물리 삭제
    int deletePostsByUserId(Long userId);                        // 조건부 삭제
    int updatePostDeleted(Long id);                              // 논리 삭제
}
```

### SQL 동사 매핑
| SQL 구문 | Mapper 동사 | 반환 타입 | 예시 |
|----------|-------------|-----------|------|
| `SELECT` | `select` | `Entity`, `List<Entity>` | `selectPostById()` |
| `SELECT COUNT` | `count` | `int`, `long` | `countPosts()` |
| `SELECT EXISTS` | `exists` | `boolean` | `existsPostById()` |
| `INSERT` | `insert` | `int` (affected rows) | `insertPost()` |
| `UPDATE` | `update` | `int` (affected rows) | `updatePost()` |
| `DELETE` | `delete` | `int` (affected rows) | `deletePost()` |

---

## 📋 5. DTO Layer

### 5-1. Request DTO

#### 명명 규칙: `{도메인명}{동작}{Request}`

```java
// 기본 CRUD
PostCreateRequest.java          // 게시글 생성 요청
PostUpdateRequest.java          // 게시글 전체 수정 요청  
PostPatchRequest.java           // 게시글 부분 수정 요청

// 특화 기능
PostSearchRequest.java          // 게시글 검색 요청
PostPublishRequest.java         // 게시글 발행 요청
PostBulkDeleteRequest.java      // 게시글 일괄 삭제 요청
```

#### 구조 예시
```java
@Getter @Setter
public class PostCreateRequest {
    @NotBlank private String title;
    @NotBlank private String content; 
    private List<String> tags;
    private String status = "DRAFT";
}

@Getter @Setter
public class PostSearchRequest {
    private String keyword;
    private String ownerUserId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> statuses;
    private String sortBy = "regDt";
    private String sortOrder = "desc";
    private int page = 0;
    private int size = 20;
}
```

### 5-2. Response DTO

#### 명명 규칙: `{도메인명}{화면/용도}{Response}`

```java
// 화면별 최적화
PostListResponse.java           // 목록 화면용
PostDetailResponse.java         // 상세 화면용  
PostSummaryResponse.java        // 요약/대시보드용
PostCardResponse.java           // 카드형 레이아웃용

// 기능별 특화
PostSearchResponse.java         // 검색 결과용
PostCreateResponse.java         // 생성 후 응답용
PostStatResponse.java           // 통계 정보용
```

#### 구조 예시  
```java
// 목록화면 최적화 (필요한 필드만)
@Getter @Builder
public class PostListResponse {
    private Long postNumber;
    private String title;
    private String ownerUserId; 
    private String regDt;
    private int viewCount;
    private boolean isNew;
}

// 상세화면 최적화 (모든 정보 포함)
@Getter @Builder  
public class PostDetailResponse {
    private Long postNumber;
    private String title;
    private String content;
    private String ownerUserId;
    private String regDt;
    private String mdfcnDt;
    private int viewCount;
    private int commentCount;
    private List<String> tags;
    private boolean canEdit;
    private boolean canDelete;
}
```

---

## 🔄 6. 계층간 호출 흐름 예시

### 게시글 조회 시
```
🌐 GET /posts/123
    ↓
📱 PostController.getPost(123L)  
    ↓
🏢 PostService.getPost(123L)
    ↓  
🗄️ PostMapper.selectPostById(123L)
    ↓
📊 SELECT * FROM posts WHERE id = 123
```

### 게시글 생성 시
```  
🌐 POST /posts + PostCreateRequest
    ↓
📱 PostController.createPost(request)
    ↓
🏢 PostService.createPost(request) 
    ↓
🗄️ PostMapper.insertPost(entity)
    ↓  
📊 INSERT INTO posts (title, content) VALUES (...)
```

---

## 📁 7. 패키지 구조

```
📁 domain/{domain}/
├── controller/
│   └── PostController.java
├── service/
│   ├── PostService.java
│   └── impl/
│       └── PostServiceImpl.java  
├── mapper/
│   └── PostMapper.java
├── dto/
│   ├── request/
│   │   ├── PostCreateRequest.java
│   │   ├── PostUpdateRequest.java
│   │   └── PostSearchRequest.java
│   └── response/
│       ├── PostListResponse.java
│       ├── PostDetailResponse.java
│       └── PostCreateResponse.java
└── entity/
    └── PostEntity.java
```

---

## ✅ 8. 체크리스트

### Controller
- [ ] HTTP Method에 맞는 동사 사용 (`getXxx`, `createXxx`)
- [ ] RESTful URI 패턴 준수
- [ ] Request/Response DTO 명확히 구분

### Service  
- [ ] 비즈니스 관점의 동사 사용 (`getXxx`, `createXxx`)
- [ ] 도메인 로직 중심의 메서드명
- [ ] 반환 타입에 따른 메서드명 구분

### Mapper
- [ ] SQL 동사 사용 (`selectXxx`, `insertXxx`)
- [ ] 데이터베이스 관점의 명명
- [ ] 반환 타입과 메서드명 일치

### DTO
- [ ] Request: `{도메인}{동작}Request` 패턴
- [ ] Response: `{도메인}{화면/용도}Response` 패턴  
- [ ] 화면 요구사항에 최적화된 필드 구성

---

## 🔍 9. 자주 묻는 질문 (FAQ)

**Q: Service와 Mapper에서 왜 다른 동사를 사용하나요?**  
A: Service는 비즈니스 관점(`getPost` - "게시글을 가져온다"), Mapper는 데이터 접근 관점(`selectPost` - "게시글을 조회한다")이기 때문입니다.

**Q: DTO는 왜 도메인명이 먼저 오나요?**  
A: DTO는 데이터 구조를 나타내는 클래스이므로 "게시글의 생성 요청"처럼 명사 중심으로 명명하기 때문입니다.

**Q: 하나의 도메인에 Response가 너무 많아지면 어떡하나요?**  
A: 실제 사용되는 화면별로만 만들고, 비슷한 구조면 통합하여 사용하세요. 과도한 세분화보다는 적절한 추상화가 중요합니다.

---

## 📚 참고자료

- RESTful API Design Guidelines
- Spring Boot Best Practices  
- Clean Code - Naming Conventions
- MyBatis Mapper Naming Conventions

---
> 이 문서는 프로젝트 진행 중 지속적으로 업데이트됩니다.