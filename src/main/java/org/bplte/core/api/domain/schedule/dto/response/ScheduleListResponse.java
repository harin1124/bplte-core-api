package org.bplte.core.api.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bplte.core.api.core.swagger.SwaggerConstant;

import java.time.LocalDate;

/**
 * [응답] 스케줄 목록
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 스케줄 목록 (ScheduleListResponse)")
public class ScheduleListResponse {
	@Schema(description = "스케줄 번호 (고유값)")
	private Long scheduleNumber;

	@Schema(description = "카테고리 번호", nullable = true)
	private Long categoryNumber;

	@Schema(description = "카테고리 이름", maxLength = 20, nullable = true)
	private String categoryName;

	@Schema(description = "카테고리 컬러", minLength = 7, maxLength = 7, nullable = true, example = "#FFFFFF")
	private String categoryColor;

	@Schema(description = "제목", minLength = 1, maxLength = 200)
	private String title;

	@Schema(description = "시작 일", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private LocalDate startDt;

	@Schema(description = "종료 일", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private LocalDate endDt;
}
