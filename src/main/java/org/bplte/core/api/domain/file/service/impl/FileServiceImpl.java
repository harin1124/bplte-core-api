package org.bplte.core.api.domain.file.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.file.dto.request.FileListRequest;
import org.bplte.core.api.domain.file.entity.FileEntity;
import org.bplte.core.api.domain.file.enums.FileRefType;
import org.bplte.core.api.domain.file.enums.FileRoleType;
import org.bplte.core.api.domain.file.mapper.FileMapper;
import org.bplte.core.api.domain.file.service.FileService;
import org.bplte.core.api.global.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileMapper fileMapper;

	@Value("${file.root}")
	private String ROOT_PATH;

	@Override
	public void saveFileList(List<MultipartFile> fileList, String refId, String rgtrId, FileRefType refType, FileRoleType roleType) {
		List<FileEntity> fileEntityList = fileListPhysicsSave(fileList, refId, rgtrId, refType, roleType);
		fileListLogicSave(fileEntityList);
	}

	@Override
	public List<FileEntity> selectFileList(FileListRequest param) {
		return fileMapper.selectFileList(param);
	}

	private List<FileEntity> fileListPhysicsSave(List<MultipartFile> fileList, String refId, String rgtrId, FileRefType refType, FileRoleType roleType) {
		List<FileEntity> fileInfoList = new ArrayList<>(fileList.size());
		int sortOrder = 1;

		for(MultipartFile file : fileList) {
			String fileName = file.getOriginalFilename();

			log.warn("저장 파일 명 ::: {}", fileName);

			// 확장자 위치
			int extensionStartAt = fileName.lastIndexOf(".");
			// 확장자명
			String extensionName = fileName.substring(extensionStartAt + 1);
			// 원본파일명 (확장자 제외)
			String fileOriginalName = fileName.substring(0, extensionStartAt);
			// 저장명 (난수화)
			String storedName = RandomStringGenerator.generateExactly(50);

			// 경로 생성
			String uniquePath = "/" + refType.getCode() + "/" + refId;
			Path uploadDir = Paths.get(ROOT_PATH + uniquePath);
			if (Files.notExists(uploadDir)) {
				try {
					Files.createDirectories(uploadDir);
				} catch(IOException ex) {
					log.info("폴더 경로를 정상적으로 생성할 수 없습니다. ({})", ex.getClass());
					throw new ApiException(ResponseCodeGeneral.UNKNOWN);
				}
			}

			// 파일 생성
			Path target = uploadDir.resolve(storedName + "." + extensionName);
			try {
				Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			} catch(IOException ex) {
				log.info("파일 생성에 실패하였습니다. ({})", ex.getClass());
				throw new ApiException(ResponseCodeGeneral.UNKNOWN);
			}

			FileEntity fileEntity = new FileEntity();
			fileEntity.setOriginalName(fileOriginalName);
			fileEntity.setStoredName(storedName);
			fileEntity.setStoredPath(uniquePath);
			fileEntity.setExtension(extensionName);
			fileEntity.setRefType(refType);
			fileEntity.setRefId(refId);
			fileEntity.setSortOrder(sortOrder++);
			fileEntity.setRoleType(roleType);
			fileEntity.setRgtrId(rgtrId);
			fileEntity.setMdfrId(rgtrId);
			fileInfoList.add(fileEntity);
		}

		return fileInfoList;
	}

	private void fileListLogicSave(List<FileEntity> fileList) {
		fileMapper.insertFileList(fileList);
	}
}
