package org.bplte.core.api.domain.schedule.service;

import org.bplte.core.api.domain.schedule.dto.request.ScheduleCreateRequest;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleListRequest;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleDetailResponse;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleListResponse;

import java.util.List;

public interface ScheduleService {
	List<ScheduleListResponse> getSchedules(ScheduleListRequest	request);
	ScheduleDetailResponse getSchedule(String currentUserId, Long scheduleNumber);
	void createSchedule(ScheduleCreateRequest request);
}
