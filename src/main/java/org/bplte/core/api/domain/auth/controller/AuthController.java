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
	
	@GetMapping("/test")
	public String test() {
		return "test. one - two.";
	}
	
	@PostMapping("/join")
	public ApiResponse<Integer> join(@RequestBody AuthJoinRequest request) {
		return ApiResponse.success(authService.join(request));
	}
}
