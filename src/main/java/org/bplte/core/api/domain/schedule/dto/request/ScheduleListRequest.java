package org.bplte.core.api.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.bplte.core.api.domain.schedule.enums.ScheduleSearchUnit;

import java.time.LocalDate;

/**
 * [요청] 스케줄 목록 조회
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 스케줄 목록 조회 (ScheduleListRequest)")
public class ScheduleListRequest {
	@Schema(description = "스케줄 검색 단위 타입")
	private ScheduleSearchUnit scheduleSearchUnit;
	@Schema(description = "스케줄 검색 날짜")
	private LocalDate scheduleSearchDt;
	@Schema(description = "카테고리 아이디")
	private Long categoryId;
	@Schema(hidden = true)
	private String requestUserId;

	@AssertTrue(message = "스케줄 검색 단위를 사용하려면 검색 날짜가 필요합니다.")
	@JsonIgnore
	@Schema(hidden = true)
	public boolean isSearchConditionValid() {
		return scheduleSearchUnit == null || scheduleSearchDt != null;
	}

	@JsonIgnore
	@Schema(hidden = true)
	public LocalDate getRangeStart() {
		return scheduleSearchUnit != null ? scheduleSearchUnit.rangeStart(scheduleSearchDt) : null;
	}

	@JsonIgnore
	@Schema(hidden = true)
	public LocalDate getRangeEnd() {
		return scheduleSearchUnit != null ? scheduleSearchUnit.rangeEnd(scheduleSearchDt) : null;
	}
}
