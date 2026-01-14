package org.bplte.core.api.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;
import org.bplte.core.api.domain.auth.dto.request.AuthLoginRequest;
import org.bplte.core.api.domain.auth.dto.response.AuthLoginResponse;
import org.bplte.core.api.domain.auth.property.AuthProperties;
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
	public ApiResponse<AuthLoginResponse> login(@RequestBody AuthLoginRequest request, HttpServletResponse response) {
		AuthLoginResponse loginInfo = authService.login(request);
		
		// HttpOnly 쿠키로 토큰 설정
		Cookie cookie = new Cookie(AuthProperties.accessToken, loginInfo.getAccessToken());
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60);
		cookie.setAttribute("SameSite", "Strict"); // CSRF 방지
		
		response.addCookie(cookie);
		
		return ApiResponse.success(loginInfo);
	}
	
	@PostMapping("/logout")
	public ApiResponse<Void> logout(HttpServletResponse response) {
		// 쿠키 삭제
		Cookie cookie = new Cookie(AuthProperties.accessToken, "");
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		
		return ApiResponse.success();
	}
}
