package org.bplte.core.api.domain.file.service;

import org.bplte.core.api.domain.file.entity.FileEntity;
import org.bplte.core.api.domain.file.enums.FileRefType;
import org.bplte.core.api.domain.file.enums.FileRoleType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
	List<FileEntity> fileListPhysicsSave(List<MultipartFile> fileList, String refId, String rgtrId, FileRefType refType, FileRoleType roleType);
	int fileListLogicSave(List<FileEntity> fileList);
}
