package org.bplte.core.api.domain.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleCreateRequest;
import org.bplte.core.api.domain.schedule.dto.request.ScheduleListRequest;
import org.bplte.core.api.domain.schedule.dto.response.ScheduleListResponse;
import org.bplte.core.api.domain.schedule.entity.ScheduleEntity;
import org.bplte.core.api.domain.schedule.mapper.ScheduleMapper;
import org.bplte.core.api.domain.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	private final ScheduleMapper scheduleMapper;

	@Override
	public List<ScheduleListResponse> getSchedules(ScheduleListRequest request) {
		return scheduleMapper.selectScheduleList(request);
	}

	@Override
	@Transactional
	public void createSchedule(ScheduleCreateRequest request) {
		// 존재하는 카테고리 아이디인지 확인
		if(request.getCategoryNumber() == null) {
			// TODO 유효성 검사 진행
			System.out.println("유효성 검사 진행");
		}

		// 시작일과 종료일의 밸리데이션
		if(!request.isDateRangeValid()) {
			throw new ApiException(ResponseCodeGeneral.BAD_REQUEST);
		}

		scheduleMapper.insertSchedule(ScheduleEntity.createToEntity(request));
	}
}
