package org.bplte.core.api.domain.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 참조 구분
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FileRefType {
	/** 게시글 첨부파일 */
	POST_ATTACHMENT("POST_ATTACHMENT"),
	/** 사용자 아이콘 */
	USER_ICON("USER_ICON");

	private String code;
}
