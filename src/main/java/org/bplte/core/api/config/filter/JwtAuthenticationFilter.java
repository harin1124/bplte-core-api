package org.bplte.core.api.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bplte.core.api.config.jwt.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = resolveToken(request);
		
		if (token != null && jwtTokenProvider.validateToken(token)) {
			String userId = jwtTokenProvider.getUserId(token);
			List<String> roles = jwtTokenProvider.getRoles(token);
			
			// Spring Security 인증 객체 생성
			List<GrantedAuthority> authorities = roles.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
			
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userId, null, authorities);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest request) {
		// 1. 쿠키에서 토큰 확인 (우선순위)
		if (request.getCookies() != null) {
			for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
				if ("accessToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		
		// 2. Authorization 헤더에서 토큰 확인 (하위 호환성)
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
}