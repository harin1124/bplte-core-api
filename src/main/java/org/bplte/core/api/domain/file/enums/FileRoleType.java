package org.bplte.core.api.domain.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 역할 구분
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FileRoleType {
	NONE("NONE"),
	THUMBNAIL("THUMBNAIL"),
	ORIGINAL("ORIGINAL");

	private String code;
}
