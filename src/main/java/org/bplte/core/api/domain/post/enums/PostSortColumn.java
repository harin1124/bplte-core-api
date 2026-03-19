package org.bplte.core.api.domain.post.enums;

import lombok.Getter;

/**
 * 정렬 컬럼 매핑
 */
@Getter
public enum PostSortColumn {
	POST_NUMBER("postNumber", "POST_NUMBER"),
	OWNER_USER_ID("ownerUserId", "OWNER_USER_ID"),
	TITLE("title", "TITLE"),
	INQ_CNT("inqCnt", "INQ_CNT"),
	REG_DT("regDt", "REG_DT"),
	RGTR_ID("rgtrId", "RGTR_ID"),
	RGTR_NAME("rgtrName", "RGTR_NAME"),
	RGTR_INFO("rgtrInfo", "RGTR_INFO");
	
	private final String apiKey;
	private final String dbColumn;
	
	PostSortColumn(String apiKey, String dbColumn) {
		this.apiKey = apiKey;
		this.dbColumn = dbColumn;
	}
	
	/**
	 * API에서 넘어온 sortColumnName에 해당하는 enum을 찾아 DB 컬럼명 반환.
	 * 없으면 null → 기본 정렬 사용.
	 */
	public static String toDbColumn(String sortColumnName) {
		if (sortColumnName == null || sortColumnName.isBlank()) {
			return null;
		}
		String key = sortColumnName.trim();
		for (PostSortColumn col : values()) {
			if (col.apiKey.equals(key)) {
				return col.dbColumn;
			}
		}
		return null;
	}
}
