package org.bplte.core.api.domain.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleCreateRequest;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleListRequest;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleDetailResponse;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleListResponse;
import org.bplte.core.api.domain.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "스케줄")
public class ScheduleController {
	private final ScheduleService scheduleService;

	@GetMapping
	@Operation(summary = "스케줄 목록 조회")
	public ApiResponse<List<ScheduleListResponse>> getSchedules(
		@CurrentUser String currentUserId,
		@ModelAttribute @Valid ScheduleListRequest request
	) {
		request.setRequestUserId(currentUserId);
		return ApiResponse.success(scheduleService.getSchedules(request));
	}

	@GetMapping("/{id}")
	@Operation(summary = "스케줄 상세 조회")
	public ApiResponse<ScheduleDetailResponse> getSchedule(
		@CurrentUser String currentUserId,
		@PathVariable Long id
	) {
		return ApiResponse.success(scheduleService.getSchedule(currentUserId, id));
	}

	@PostMapping
	@Operation(summary = "스케줄 등록")
	public ApiResponse<Void> createSchedule(
		@CurrentUser String currentUserId,
		@RequestBody @Valid ScheduleCreateRequest request
	) {
		request.setRequestUserId(currentUserId);
		scheduleService.createSchedule(request);
		return ApiResponse.success();
	}

	@PutMapping("/{id}")
	@Operation(summary = "스케줄 수정")
	public ApiResponse<Void> updateSchedule(@PathVariable Long id) {
		return ApiResponse.success();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "스케줄 삭제")
	public ApiResponse<Void> deleteSchedule(@PathVariable Long id) {
		return ApiResponse.success();
	}
}
