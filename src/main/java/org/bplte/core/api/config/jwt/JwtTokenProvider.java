package org.bplte.core.api.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final JwtProperties jwtProperties;
	private SecretKey key;
	
	@PostConstruct
	public void init() {
		// HMAC-SHA 알고리즘을 위한 키 생성
		byte[] keyBytes = Decoders.BASE64.decode(
				Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes())
		);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	// Access Token 생성
	public String createAccessToken(String userId, List<String> roles) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());
		
		return Jwts.builder()
				.subject(userId)
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}
	
	// Refresh Token 생성
	public String createRefreshToken(String userId) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());
		
		return Jwts.builder()
				.subject(userId)
				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}
	
	// 토큰에서 사용자 정보 추출
	public String getUserId(String token) {
		return parseClaims(token).getSubject();
	}
	
	// 토큰에서 권한 정보 추출
	@SuppressWarnings("unchecked")
	public List<String> getRoles(String token) {
		return parseClaims(token).get("roles", List.class);
	}
	
	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("JWT token is malformed: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT token compact is empty: {}", e.getMessage());
		}
		return false;
	}
	
	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}