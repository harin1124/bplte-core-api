package org.bplte.core.api.domain.schedule.entity;

import lombok.*;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleCreateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 스케줄
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity {
	/** 스케줄 번호 */
	private Long scheduleNumber;
	/** 카테고리 번호 */
	private Long categoryNumber;
	/** 제목 */
	private String title;
	/** 시작일 */
	private LocalDate startDt;
	/** 종료일 */
	private LocalDate endDt;
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

	public static ScheduleEntity createToEntity(ScheduleCreateRequest request) {
		return ScheduleEntity.builder()
			.categoryNumber(request.getCategoryNumber())
			.title(request.getTitle())
			.startDt(request.getStartDt())
			.endDt(request.getEndDt())
			.delYn("N")
			.rgtrId(request.getRequestUserId())
			.mdfrId(request.getRequestUserId())
			.build();
	}
}
