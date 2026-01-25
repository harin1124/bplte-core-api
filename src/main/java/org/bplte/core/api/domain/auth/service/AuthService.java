package org.bplte.core.api.domain.auth.service;

import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;
import org.bplte.core.api.domain.auth.dto.request.AuthLoginRequest;
import org.bplte.core.api.domain.auth.dto.response.AuthLoginResponse;

public interface AuthService {
	int join(AuthJoinRequest request);
	AuthLoginResponse login(AuthLoginRequest request);
	boolean availableUserId(String userId);
	boolean availableEmail(String email);
}
