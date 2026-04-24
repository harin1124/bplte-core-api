# 사용자 테이블
CREATE TABLE TBL_USER (
    USER_ID   VARCHAR(30)                          NOT NULL COMMENT '사용자_아이디' PRIMARY KEY,
    USER_NAME VARCHAR(30)                          NOT NULL COMMENT '사용자_이름',
    EMAIL     VARCHAR(64)                          NOT NULL COMMENT '이메일',
    SALT      CHAR(64)                             NOT NULL COMMENT 'SALT',
    PASSWORD  CHAR(64)                             NOT NULL COMMENT 'PASSWORD',
    DEL_YN    CHAR     DEFAULT 'N'                 NOT NULL COMMENT '삭제_여부',
    REG_DT    DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID   VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT  DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID   VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디'
) COMMENT '사용자정보기본';

# 포스트 테이블
CREATE TABLE TBL_POST (
    POST_NUMBER    INT AUTO_INCREMENT                   COMMENT '포스트_번호' PRIMARY KEY,
    OWNER_USER_ID  VARCHAR(30)                          NOT NULL COMMENT '소유자_사용자_아이디',
    TITLE          VARCHAR(100)                         NULL COMMENT '제목',
    CONTENT        LONGTEXT                             NULL COMMENT '내용',
    SEARCH_CONTENT LONGTEXT                             NULL COMMENT '검색_내용',
    INQ_CNT        INT                                  NOT NULL,
    DEL_YN         CHAR     DEFAULT 'N'                 NOT NULL COMMENT '삭제_여부',
    REG_DT         DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID        VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT       DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID        VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디'
) COMMENT '포스트기본';
CREATE INDEX IDX_TBL_POST_OWNER_USER_ID ON TBL_POST (OWNER_USER_ID);

# 첨부파일 테이블
CREATE TABLE TBL_FILE (
    FILE_ID       BIGINT AUTO_INCREMENT                COMMENT '첨부파일_아이디' PRIMARY KEY,
    ORIGINAL_NAME VARCHAR(300)                         NOT NULL COMMENT '원본_이름',
    STORED_NAME   VARCHAR(300)                         NOT NULL COMMENT '저장_이름',
    STORED_PATH   VARCHAR(300)                         NOT NULL COMMENT '저장_경로',
    EXTENSION     VARCHAR(10)                          NOT NULL COMMENT '확장자',
    REF_TYPE      VARCHAR(50)                          NOT NULL COMMENT '참조_구분',
    REF_ID        VARCHAR(50)                          NOT NULL COMMENT '참조_아이디',
    SORT_ORDER    INT                                  NOT NULL COMMENT '정렬_순서',
    ROLE_TYPE     VARCHAR(50)                          NOT NULL COMMENT '역할_구분',
    DEL_YN        CHAR                                 NOT NULL COMMENT '삭제_여부',
    REG_DT        DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID       VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT      DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID       VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디'
) COMMENT '파일_기본';

