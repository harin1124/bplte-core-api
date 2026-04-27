package org.bplte.core.api.domain.file.dto.request;

import lombok.*;
import org.bplte.core.api.domain.file.enums.FileRefType;

import java.util.List;

/**
 * [요청] 파일 목록 삭제
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFileListInput {
	/** 파일 이름 목록 */
	private List<Long> fileIdList;
	/** 참조 구분 */
	private FileRefType refType;
	/** 참조 아이디 */
	private String refId;
	/** 수정자 아이디 */
	private String mdfrId;
}
