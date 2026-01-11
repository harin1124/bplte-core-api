package org.bplte.core.api.domain.auth.service;

import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;
import org.bplte.core.api.domain.auth.dto.request.AuthLoginRequest;

public interface AuthService {
	int join(AuthJoinRequest request);
	String login(AuthLoginRequest request);
}
