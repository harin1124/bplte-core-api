package org.bplte.core.api.domain.file.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.file.entity.FileEntity;

import java.util.List;

@Mapper
public interface FileMapper {
	int insertFileList(List<FileEntity> param);
}
