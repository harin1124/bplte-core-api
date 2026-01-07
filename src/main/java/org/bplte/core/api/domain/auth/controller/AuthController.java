package org.bplte.core.api.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;
import org.bplte.core.api.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/join")
	public ApiResponse<Integer> join(@RequestBody AuthJoinRequest request) {
		return ApiResponse.success(authService.join(request));
	}
	
	@PostMapping("/login")
	public ApiResponse<String> login() {
		return ApiResponse.success("login");
	}
}
