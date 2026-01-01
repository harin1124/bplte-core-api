package org.bplte.core.api.domain.auth.service;

import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;

public interface AuthService {
	int join(AuthJoinRequest request);
}
