package org.bplte.core.api.domain.schedule.entity;

import lombok.*;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleCreateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 스케줄 카테고리
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCategoryEntity {
	/** 카테고리 번호 */
	private Long categoryNumber;
	/** 소유자 사용자 아이디 */
	private String ownerUserId;
	/** 카테고리 이름 */
	private String categoryName;
	/** 컬러 */
	private String color;
	/** 기본 여부 */
	private String defaultYn;
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
