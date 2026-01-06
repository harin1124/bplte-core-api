package org.bplte.core.api.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;
import org.bplte.core.api.domain.auth.mapper.AuthMapper;
import org.bplte.core.api.domain.auth.service.AuthService;
import org.bplte.core.api.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthMapper authMapper;
	
	@Override
	public int join(AuthJoinRequest request) {
		request.setRgtrId(request.getUserId());
		request.setMdfrId(request.getUserId());
		request.setSalt("test");
		return authMapper.userInsert(UserEntity.toEntity(request));
	}
}
