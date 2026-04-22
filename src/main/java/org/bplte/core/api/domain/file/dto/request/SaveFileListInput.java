package org.bplte.core.api.domain.file.dto.request;

import lombok.*;
import org.bplte.core.api.domain.file.enums.FileRefType;
import org.bplte.core.api.domain.file.enums.FileRoleType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [요청] 파일 목록 저장
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveFileListInput {
	private List<MultipartFile> fileList;
	private List<String> fileNameOrderList;
	private String refId;
	private String rgtrId;
	private FileRefType refType;
	private FileRoleType roleType;
}
