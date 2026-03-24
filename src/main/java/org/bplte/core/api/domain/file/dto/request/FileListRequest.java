package org.bplte.core.api.domain.file.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bplte.core.api.domain.file.enums.FileRefType;
import org.bplte.core.api.domain.file.enums.FileRoleType;

/**
 * [요청] 파일 목록 조회
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 파일 목록 조회 (FileListRequest)")
public class FileListRequest {
	private FileRefType refType;
	private String refId;
	private FileRoleType roleType;
}