package org.bplte.core.api.domain.schedule.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleListRequest;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleDetailResponse;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleListResponse;
import org.bplte.core.api.domain.schedule.entity.ScheduleEntity;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ScheduleMapper {
	void insertSchedule(ScheduleEntity schedule);
	List<ScheduleListResponse> selectScheduleList(ScheduleListRequest request);
	Optional<ScheduleDetailResponse> selectSchedule(String currentUserId, Long scheduleNumber);
}
