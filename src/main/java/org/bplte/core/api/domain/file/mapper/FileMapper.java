package org.bplte.core.api.domain.file.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.file.dto.request.DeleteFileListInput;
import org.bplte.core.api.domain.file.dto.request.FileListRequest;
import org.bplte.core.api.domain.file.entity.FileEntity;

import java.util.List;

@Mapper
public interface FileMapper {
	void insertFileList(List<FileEntity> param);
	void deleteFileList(DeleteFileListInput param);
	List<FileEntity> selectFileList(FileListRequest param);
	void updateFileSortOrder(FileEntity param);
}
