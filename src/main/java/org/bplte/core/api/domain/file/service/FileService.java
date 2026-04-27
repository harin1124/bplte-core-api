package org.bplte.core.api.domain.file.service;

import org.bplte.core.api.domain.file.dto.request.DeleteFileListInput;
import org.bplte.core.api.domain.file.dto.request.FileListRequest;
import org.bplte.core.api.domain.file.dto.request.SaveFileListInput;
import org.bplte.core.api.domain.file.entity.FileEntity;
import org.bplte.core.api.domain.file.enums.FileRefType;
import org.bplte.core.api.domain.file.enums.FileRoleType;

import java.util.List;

public interface FileService {
	void saveFileList(SaveFileListInput param);
	void deleteFileList(DeleteFileListInput param);
	List<FileEntity> selectFileList(FileListRequest param);

	/**
	 * 참조 단위로 남아 있는 첨부의 표시 순서를 확장자 포함 원본 파일명 목록에 맞게 갱신한다.
	 * 목록에 없는 파일이 있거나, 목록의 이름에 해당하는 파일이 없으면 {@code BAD_REQUEST}로 거절한다.
	 */
	void reorderFilesByOriginalName(FileRefType refType, String refId, FileRoleType roleType, List<String> fileNameOrderList, String mdfrId);
}
