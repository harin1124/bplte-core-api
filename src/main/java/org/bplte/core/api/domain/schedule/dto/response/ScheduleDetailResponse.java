package org.bplte.core.api.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bplte.core.api.core.swagger.SwaggerConstant;

/**
 * [응답] 스케줄 상세
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 스케줄 상세 (ScheduleDetailResponse)")
public class ScheduleDetailResponse {
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
	private String startDt;

	@Schema(description = "종료 일", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private String endDt;

	@Schema(description = "등록 일시", format = SwaggerConstant.DATE_TIME_DEFAULT, example = SwaggerConstant.DATE_TIME_EXAMPLE)
	private String regDt;

	@Schema(description = "등록자 아이디", minLength = 1, maxLength = 30)
	private String rgtrId;

	@Schema(description = "등록자 명", minLength = 1, maxLength = 30)
	private String rgtrName;

	@Schema(description = "등록자 정보", format = SwaggerConstant.USER_INFO_DEFAULT, example = SwaggerConstant.USER_INFO_EXAMPLE)
	private String rgtrInfo;

	@Schema(description = "수정 일시", format = SwaggerConstant.DATE_TIME_DEFAULT, example = SwaggerConstant.DATE_TIME_EXAMPLE)
	private String mdfcnDt;

	@Schema(description = "수정자 아이디", minLength = 1, maxLength = 30)
	private String mdfrId;

	@Schema(description = "수정자 명", minLength = 1, maxLength = 30)
	private String mdfrName;

	@Schema(description = "수정자 정보", format = SwaggerConstant.USER_INFO_DEFAULT, example = SwaggerConstant.USER_INFO_EXAMPLE)
	private String mdfrInfo;
}
