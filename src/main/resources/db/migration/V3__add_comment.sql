# 댓글 테이블
# - 무제한 깊이 대댓글(자기참조 PARENT_COMMENT_ID)
# - 스레드 그룹 식별용 ROOT_COMMENT_ID, 표시용 DEPTH
# - 등록/수정/삭제(DEL_YN 소프트 삭제) 지원
# - SECRET_YN='Y' 인 경우 글 작성자 및 본인만 조회 가능(앱 레벨 가시성 제어)
# - LIKE_CNT/DISLIKE_CNT 는 TBL_COMMENT_REACTION 의 비정규화 캐시(트랜잭션 안에서 +/- 1)
# - 논리 FK: POST_NUMBER -> TBL_POST.POST_NUMBER, PARENT_COMMENT_ID/ROOT_COMMENT_ID -> TBL_COMMENT.COMMENT_ID
CREATE TABLE TBL_COMMENT (
    COMMENT_ID        BIGINT AUTO_INCREMENT                COMMENT '댓글_아이디' PRIMARY KEY,
    POST_NUMBER       INT                                  NOT NULL COMMENT '포스트_번호',
    PARENT_COMMENT_ID BIGINT                               NULL     COMMENT '상위_댓글_아이디(NULL=최상위)',
    ROOT_COMMENT_ID   BIGINT                               NULL     COMMENT '루트_댓글_아이디(스레드_그룹)',
    DEPTH             INT      DEFAULT 0                   NOT NULL COMMENT '댓글_깊이(0=최상위)',
    CONTENT           TEXT                                 NOT NULL COMMENT '내용',
    LIKE_CNT          INT      DEFAULT 0                   NOT NULL COMMENT '좋아요_수',
    DISLIKE_CNT       INT      DEFAULT 0                   NOT NULL COMMENT '싫어요_수',
    SECRET_YN         CHAR(1)  DEFAULT 'N'                 NOT NULL COMMENT '비밀_댓글_여부(Y=글작성자/본인만_조회)',
    DEL_YN            CHAR(1)  DEFAULT 'N'                 NOT NULL COMMENT '삭제_여부',
    REG_DT            DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID           VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT          DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID           VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디'
) COMMENT '댓글_기본';

# 포스트별 댓글 조회/정렬용(WHERE POST_NUMBER=? AND DEL_YN='N' ORDER BY REG_DT)
CREATE INDEX IDX_TBL_COMMENT_POST    ON TBL_COMMENT (POST_NUMBER, DEL_YN, REG_DT);
# 자식 댓글 조회/카운트
CREATE INDEX IDX_TBL_COMMENT_PARENT  ON TBL_COMMENT (PARENT_COMMENT_ID);
# 스레드 단위 조회
CREATE INDEX IDX_TBL_COMMENT_ROOT    ON TBL_COMMENT (ROOT_COMMENT_ID);
# 작성자별 조회(마이페이지/관리)
CREATE INDEX IDX_TBL_COMMENT_RGTR_ID ON TBL_COMMENT (RGTR_ID);

# 댓글 좋아요/싫어요 테이블
# - 한 댓글에 한 사용자는 좋아요 OR 싫어요 중 단 1건만 가능 → UNIQUE (COMMENT_ID, USER_ID)
# - 동일 반응 중복(좋아요 2번 등) 불가 → 위 UNIQUE 제약으로 동시 보장
# - 좋아요 ↔ 싫어요 변경/취소는 같은 행을 UPDATE (INSERT ... ON DUPLICATE KEY UPDATE 권장)
# - DEL_YN='Y' 는 "반응 취소" 상태(다시 누르면 동일 행을 UPDATE 로 복원)
# - 누가 어떤 반응을 했는지 추적 가능: (COMMENT_ID, USER_ID, REACTION_TYPE)
# - 논리 FK: COMMENT_ID -> TBL_COMMENT.COMMENT_ID, USER_ID -> TBL_USER.USER_ID
CREATE TABLE TBL_COMMENT_REACTION (
    REACTION_ID   BIGINT AUTO_INCREMENT                COMMENT '반응_아이디' PRIMARY KEY,
    COMMENT_ID    BIGINT                               NOT NULL COMMENT '댓글_아이디',
    USER_ID       VARCHAR(30)                          NOT NULL COMMENT '사용자_아이디',
    REACTION_TYPE VARCHAR(10)                          NOT NULL COMMENT '반응_구분(LIKE/DISLIKE)',
    DEL_YN        CHAR(1)  DEFAULT 'N'                 NOT NULL COMMENT '삭제_여부',
    REG_DT        DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID       VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT      DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID       VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디',
    CONSTRAINT UK_TBL_COMMENT_REACTION       UNIQUE (COMMENT_ID, USER_ID),
    CONSTRAINT CHK_TBL_COMMENT_REACTION_TYPE CHECK  (REACTION_TYPE IN ('LIKE','DISLIKE'))
) COMMENT '댓글_반응';

# 댓글별 반응 집계(좋아요/싫어요 수 COUNT, 누가 눌렀는지 목록 조회)
CREATE INDEX IDX_TBL_COMMENT_REACTION_COMMENT ON TBL_COMMENT_REACTION (COMMENT_ID, REACTION_TYPE, DEL_YN);
# 사용자별 반응 이력 조회(마이페이지/관리)
CREATE INDEX IDX_TBL_COMMENT_REACTION_USER    ON TBL_COMMENT_REACTION (USER_ID);
