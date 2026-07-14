package org.bplte.core.api.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * [요청] 스케줄 등록
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 스케줄 등록 (ScheduleCreateRequest)")
public class ScheduleCreateRequest {
	@Schema(description = "카테고리 번호")
	private Long categoryNumber;
	@NotBlank
	@Size(min = 1, max = 200)
	@Schema(description = "제목")
	private String title;
	@NotNull
	@Schema(description = "시작일")
	private LocalDate startDt;
	@NotNull
	@Schema(description = "종료일")
	private LocalDate endDt;
	@Schema(hidden = true)
	private String requestUserId;

	@AssertTrue(message = "시작일은 종료일보다 늦을 수 없습니다.")
	@JsonIgnore
	@Schema(hidden = true)
	public boolean isDateRangeValid() {
		return startDt == null || endDt == null || !startDt.isAfter(endDt);
	}
}
