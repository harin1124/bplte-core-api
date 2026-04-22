package org.bplte.core.api.domain.file.service;

import org.bplte.core.api.domain.file.dto.request.FileListRequest;
import org.bplte.core.api.domain.file.dto.request.SaveFileListInput;
import org.bplte.core.api.domain.file.entity.FileEntity;

import java.util.List;

public interface FileService {
	void saveFileList(SaveFileListInput param);
	List<FileEntity> selectFileList(FileListRequest param);
}
