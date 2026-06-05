# 댓글 내용 컬럼 위지윅 에디터 대응
# - TEXT(약 64KB) -> LONGTEXT(약 4GB) 로 확장
# - 위지윅 에디터의 HTML/스타일/이미지(base64 인라인 등) 본문 수용
# - 빈 댓글 방지를 위해 NOT NULL 은 유지
ALTER TABLE TBL_COMMENT
    MODIFY COLUMN CONTENT LONGTEXT NOT NULL COMMENT '내용';
