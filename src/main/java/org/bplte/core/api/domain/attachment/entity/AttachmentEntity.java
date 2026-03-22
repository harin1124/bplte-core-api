package org.bplte.core.api.domain.attachment.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 첨부파일
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentEntity {
	/** 첨부파일 아이디 */
	private long attachmentId;
	/** 원본 이름 */
	private String originalName;
	/** 저장 이름 */
	private String storedName;
	/** 저장 경로 */
	private String storedPath;
	/** 확장자 */
	private String extension;
	/** 참조 구분 */
	private String refType;
	/** 참조 아이디 */
	private String refId;
	/** 정렬 순서 */
	private int sortOrder;
	/** 역할 구분 */
	private String roleType;
	/** 삭제 여부 */
	private String delYn;
	/** 등록 일시 */
	private LocalDateTime regDt;
	/** 등록자 아이디 */
	private String rgtrId;
	/** 수정 일시 */
	private LocalDateTime mdfcnDt;
	/** 수정자 아이디 */
	private String mdfrId;
}