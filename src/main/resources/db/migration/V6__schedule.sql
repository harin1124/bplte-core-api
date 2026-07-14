# 일정 카테고리 테이블
# - 모든 카테고리는 OWNER_USER_ID 를 가진 사용자 소유 카테고리(공용/전역 행 없음)
# - DEFAULT_YN='Y' 는 회원가입 시 자동으로 복사 생성된 기본 카테고리(개인/회사 업무 등) 표시
#   -> 삭제는 허용하되 이름 변경은 화면 정책으로 제한하는 등 UI 차등 처리에 활용
# - USE_YN='N' 은 비활성화(목록/선택에서 숨김), DEL_YN='Y' 는 소프트 삭제
# - 논리 FK: OWNER_USER_ID -> TBL_USER.USER_ID
CREATE TABLE TBL_SCHEDULE_CATEGORY (
    CATEGORY_NUMBER    BIGINT AUTO_INCREMENT                     COMMENT '카테고리_번호' PRIMARY KEY,
    OWNER_USER_ID  VARCHAR(30)                          NOT NULL COMMENT '소유자_사용자_아이디',
    CATEGORY_NAME  VARCHAR(20)                          NOT NULL COMMENT '카테고리_이름',
    COLOR          VARCHAR(7)                           NOT NULL COMMENT '카테고리_컬러(HEX,_예:_#FFFFFF)',
    DEFAULT_YN     CHAR(1)  DEFAULT 'N'                 NOT NULL COMMENT '기본_여부',
    USE_YN         CHAR(1)  DEFAULT 'Y'                 NOT NULL COMMENT '사용_여부',
    DEL_YN         CHAR(1)  DEFAULT 'N'                 NOT NULL COMMENT '삭제_여부',
    REG_DT         DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID        VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT       DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID        VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디',
    CONSTRAINT UK_TBL_SCHEDULE_CATEGORY_NAME UNIQUE (OWNER_USER_ID, CATEGORY_NAME)
) COMMENT '일정_카테고리_기본';

# 사용자별 카테고리 목록 조회(마이페이지 설정 화면 등)
CREATE INDEX IDX_TBL_SCHEDULE_CATEGORY_OWNER ON TBL_SCHEDULE_CATEGORY (OWNER_USER_ID, USE_YN, DEL_YN);

# 일정 테이블
# - 소유자는 별도 컬럼 없이 RGTR_ID(등록자)를 기준으로 "내 일정"을 구분
# - CATEGORY_NUMBER 는 TBL_SCHEDULE_CATEGORY 의 본인 소유 카테고리를 참조(응용 계층에서 RGTR_ID=OWNER_USER_ID 검증)
# - 논리 FK: CATEGORY_NUMBER -> TBL_SCHEDULE_CATEGORY.CATEGORY_NUMBER, RGTR_ID -> TBL_USER.USER_ID
CREATE TABLE TBL_SCHEDULE (
    SCHEDULE_NUMBER BIGINT AUTO_INCREMENT                     COMMENT '일정_번호' PRIMARY KEY,
    CATEGORY_NUMBER BIGINT                                    COMMENT '카테고리_번호',
    TITLE       VARCHAR(200)                         NOT NULL COMMENT '일정_제목',
    START_DT    DATE                                 NOT NULL COMMENT '시작_일',
    END_DT      DATE                                 NOT NULL COMMENT '종료_일',
    DEL_YN      CHAR(1)  DEFAULT 'N'                 NOT NULL COMMENT '삭제_여부',
    REG_DT      DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID     VARCHAR(30)                          NOT NULL COMMENT '등록자_아이디',
    MDFCN_DT    DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID     VARCHAR(30)                          NOT NULL COMMENT '수정자_아이디'
) COMMENT '일정_기본';

# 내 일정 목록/캘린더 조회(WHERE RGTR_ID=? AND DEL_YN='N' ORDER BY START_DT)
CREATE INDEX IDX_TBL_SCHEDULE_RGTR_ID  ON TBL_SCHEDULE (RGTR_ID, DEL_YN, START_DT);
# 카테고리별 일정 조회/필터
CREATE INDEX IDX_TBL_SCHEDULE_CATEGORY ON TBL_SCHEDULE (CATEGORY_NUMBER);
# 기간 조회(달력 뷰: WHERE START_DT <= ? AND END_DT >= ?)
CREATE INDEX IDX_TBL_SCHEDULE_PERIOD   ON TBL_SCHEDULE (START_DT, END_DT);
