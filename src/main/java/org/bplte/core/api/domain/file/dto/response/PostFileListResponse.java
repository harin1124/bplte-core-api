package org.bplte.core.api.domain.file.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * [응답] 파일 목록
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 파일 목록 (FileListResponse)")
public class PostFileListResponse {
	@Schema(description = "파일 아이디")
	private long fileId;
	@Schema(description = "파일 이름")
	private String fileName;
	@Schema(description = "정렬 순서")
	private int sortOrder;
}
