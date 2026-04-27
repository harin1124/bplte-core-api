package org.bplte.core.api.domain.file.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.file.dto.request.DeleteFileListInput;
import org.bplte.core.api.domain.file.dto.request.FileListRequest;
import org.bplte.core.api.domain.file.dto.request.SaveFileListInput;
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
import java.text.Normalizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileMapper fileMapper;

	@Value("${file.root}")
	private String ROOT_PATH;

	/**
	 * 업로드 파일을 저장소에 기록하고 DB에 일괄 등록한다.
	 * 원본 파일명(확장자 포함)이 중복이면 요청을 거절하며,
	 * {@link SaveFileListInput#getFileNameOrderList()}가 있으면 그 순서에 맞춰 {@link FileEntity#getSortOrder()}를 부여한다.
	 *
	 * @param param 저장 대상 파일·참조 정보·정렬 순서 목록
	 * @throws ApiException 원본 파일명이 중복된 경우 {@link ResponseCodeGeneral#BAD_REQUEST}, 디스크 IO 실패 시 {@link ResponseCodeGeneral#UNKNOWN}
	 */
	@Override
	public void saveFileList(SaveFileListInput param) {
		assertNoDuplicateOriginalFileNames(param.getFileList());

		List<FileEntity> fileEntityList = fileListPhysicsSave(
			param.getFileList(),
			param.getRefId(),
			param.getRgtrId(),
			param.getRefType(),
			param.getRoleType());

		applySortOrderByFileNameOrder(fileEntityList, param.getFileNameOrderList(), false);
		fileListLogicSave(fileEntityList);
	}

	@Override
	public void deleteFileList(DeleteFileListInput param) {
		if (param.getFileIdList() == null || param.getFileIdList().isEmpty()) {
			return;
		}
		fileMapper.deleteFileList(param);
	}

	@Override
	public void reorderFilesByOriginalName(FileRefType refType, String refId, FileRoleType roleType, List<String> fileNameOrderList, String mdfrId) {
		if (fileNameOrderList == null || fileNameOrderList.isEmpty()) {
			return;
		}
		FileListRequest listRequest = new FileListRequest();
		listRequest.setRefType(refType);
		listRequest.setRefId(refId);
		listRequest.setRoleType(roleType);
		List<FileEntity> existing = fileMapper.selectFileList(listRequest);
		if (existing.isEmpty()) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}
		applySortOrderByFileNameOrder(existing, fileNameOrderList, true);
		for (FileEntity entity : existing) {
			entity.setMdfrId(mdfrId);
			fileMapper.updateFileSortOrder(entity);
		}
	}

	/**
	 * 조건에 맞는 파일 메타데이터 목록을 조회한다.
	 *
	 * @param param 참조 유형·참조 ID 등 조회 조건
	 * @return 파일 엔티티 목록(없으면 빈 목록일 수 있음)
	 */
	@Override
	public List<FileEntity> selectFileList(FileListRequest param) {
		return fileMapper.selectFileList(param);
	}

	/**
	 * 멀티파트 파일을 루트 경로 아래 참조별 디렉터리에 복사하고, 저장에 사용할 {@link FileEntity}를 구성한다.
	 * {@link FileEntity#getSortOrder()}는 설정하지 않는다.
	 *
	 * @param fileList 업로드 파일 목록(각 항목의 원본 파일명에 확장자가 포함되어야 함)
	 * @param refId 참조 엔티티 식별자
	 * @param rgtrId 등록·수정자 ID
	 * @param refType 파일이 귀속되는 참조 구분
	 * @param roleType 파일 역할 구분
	 * @return 디스크에 반영된 내용이 반영된 파일 엔티티 목록
	 * @throws ApiException 디렉터리 생성 또는 파일 복사에 실패한 경우 {@link ResponseCodeGeneral#UNKNOWN}
	 */
	private List<FileEntity> fileListPhysicsSave(List<MultipartFile> fileList, String refId, String rgtrId, FileRefType refType, FileRoleType roleType) {
		List<FileEntity> fileInfoList = new ArrayList<>(fileList.size());

		for(MultipartFile file : fileList) {
			String fileName = canonicalFileKey(file.getOriginalFilename());

			log.warn("저장 파일 명 ::: {}", fileName);

			if(fileName == null || fileName.isEmpty()) {
				throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
			}

			// 확장자 위치
			int extensionStartAt = fileName.lastIndexOf(".");
			if (extensionStartAt < 1 || extensionStartAt == fileName.length() - 1) {
				throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
			}
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
			fileEntity.setRoleType(roleType);
			fileEntity.setRgtrId(rgtrId);
			fileEntity.setMdfrId(rgtrId);
			fileInfoList.add(fileEntity);
		}

		return fileInfoList;
	}

	/**
	 * 파일 메타데이터 목록을 DB에 삽입한다.
	 *
	 * @param fileList 삽입할 {@link FileEntity} 목록
	 */
	private void fileListLogicSave(List<FileEntity> fileList) {
		fileMapper.insertFileList(fileList);
	}

	/**
	 * 업로드 파일들의 원본 파일명(확장자 포함, 경로 제거·NFC 정규화 키 기준)에 중복이 없는지 검사한다.
	 *
	 * @param fileList 검사할 멀티파트 파일 목록; {@code null}이거나 비어 있으면 아무 것도 하지 않음
	 * @throws ApiException 동일한 원본 파일명이 둘 이상이면 {@link ResponseCodeGeneral#BAD_REQUEST}
	 */
	private void assertNoDuplicateOriginalFileNames(List<MultipartFile> fileList) {
		if (fileList == null || fileList.isEmpty()) {
			return;
		}

		Set<String> seen = new HashSet<>(fileList.size());
		for (MultipartFile file : fileList) {
			String originalFilename = file.getOriginalFilename();
			if (originalFilename == null) {
				continue;
			}
			String key = canonicalFileKey(originalFilename);
			if (key == null || key.isEmpty()) {
				continue;
			}
			if (!seen.add(key)) {
				throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
			}
		}
	}

	/**
	 * {@code fileNameOrderList}에 나온 순서대로 {@link FileEntity#setSortOrder(int)}에 1부터 번호를 부여한다.
	 * 매칭 키는 확장자를 포함한 파일명({@code originalName + "." + extension})과 목록 문자열을
	 * 경로 제거·베이스네임·유니코드 NFC 정규화 후 동등 비교한다(폼 텍스트와 멀티파트 원본명의 NFD/NFC 차이 보정).
	 * {@code fileNameOrderList}가 {@code null}이거나 비어 있으면 {@code fileEntityList}의 현재 순서대로 부여한다.
	 * 목록에 없거나 매칭되지 않은 엔티티는 남은 순번으로 뒤에 배정된다(strict 모드에서는 허용하지 않음).
	 *
	 * @param fileEntityList 정렬 순서를 채울 파일 엔티티 목록
	 * @param fileNameOrderList 원하는 저장 순서의 확장자 포함 파일명 목록; 생략 시 업로드 목록 순서 사용
	 * @param strict {@code true}이면 목록의 각 이름은 반드시 매칭되어야 하고, 매칭 후 남는 엔티티도 없어야 함
	 */
	private void applySortOrderByFileNameOrder(List<FileEntity> fileEntityList, List<String> fileNameOrderList, boolean strict) {
		if (fileNameOrderList == null || fileNameOrderList.isEmpty()) {
			if (strict) {
				throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
			}
			int order = 1;
			for (FileEntity entity : fileEntityList) {
				entity.setSortOrder(order++);
			}
			return;
		}

		List<FileEntity> pool = new ArrayList<>(fileEntityList);
		int sortOrder = 1;

		for (String orderedName : fileNameOrderList) {
			FileEntity matched = removeFirstMatchingEntity(pool, orderedName);
			if (matched == null) {
				if (strict) {
					throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
				}
				continue;
			}
			matched.setSortOrder(sortOrder++);
		}
		if (!pool.isEmpty()) {
			if (strict) {
				throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
			}
			for (FileEntity remaining : pool) {
				remaining.setSortOrder(sortOrder++);
			}
		}
	}

	/**
	 * {@code pool}에서 {@code orderedName}이 {@code originalName + "." + extension}과
	 * 동일한 정규화 키로 일치하는
	 * 첫 번째 {@link FileEntity}를 제거하고 반환한다.
	 *
	 * @param pool 아직 순번이 배정되지 않은 후보 엔티티 목록(호출 시 일치 항목이 제거될 수 있음)
	 * @param orderedName 비교할 확장자 포함 파일명; {@code null}이면 매칭하지 않음
	 * @return 제거된 엔티티; 일치 항목이 없으면 {@code null}
	 */
	private FileEntity removeFirstMatchingEntity(List<FileEntity> pool, String orderedName) {
		if (orderedName == null) {
			return null;
		}
		String orderedKey = canonicalFileKey(orderedName);
		if (orderedKey == null || orderedKey.isEmpty()) {
			return null;
		}
		for (int i = 0, size = pool.size(); i < size; i++) {
			FileEntity entity = pool.get(i);
			String entityKey = canonicalFileKey(entity.getOriginalName() + "." + entity.getExtension());
			if (orderedKey.equalsIgnoreCase(entityKey)) {
				return pool.remove(i);
			}
		}
		return null;
	}

	/**
	 * {@link MultipartFile#getOriginalFilename()} 등에 포함될 수 있는 디렉터리 경로를 제거하고 파일명만 반환한다.
	 */
	private static String basenameOnly(String fileName) {
		if (fileName == null) {
			return null;
		}
		String trimmed = fileName.trim();
		int slash = Math.max(trimmed.lastIndexOf('/'), trimmed.lastIndexOf('\\'));
		return slash >= 0 ? trimmed.substring(slash + 1) : trimmed;
	}

	/**
	 * 경로 제거·NFC·BOM 제거·확장자 소문자 통일까지 적용한 비교용 파일명 키.
	 * 클라이언트 폼과 멀티파트 원본명의 NFD/NFC 차이, UTF-8 BOM, 확장자 대소문자 차이를 줄인다.
	 */
	private static String canonicalFileKey(String fileName) {
		String base = basenameOnly(fileName);
		if (base == null || base.isEmpty()) {
			return null;
		}
		String n = Normalizer.normalize(base, Normalizer.Form.NFC).trim();
		if (!n.isEmpty() && n.charAt(0) == '\uFEFF') {
			n = n.substring(1).trim();
		}
		int dot = n.lastIndexOf('.');
		if (dot < 1 || dot == n.length() - 1) {
			return n;
		}
		String ext = n.substring(dot + 1).toLowerCase(Locale.ROOT);
		return n.substring(0, dot) + "." + ext;
	}
}
