package org.bplte.core.api.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;
import org.bplte.core.api.domain.auth.mapper.AuthMapper;
import org.bplte.core.api.domain.auth.service.AuthService;
import org.bplte.core.api.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthMapper authMapper;
	
	@Override
	public int join(AuthJoinRequest request) {
		// 랜덤 SALT 생성
		String salt = generateSalt();
		
		// 비밀번호 암호화
		String encryptedPassword = encryptPassword(request.getPassword(), salt);
		
		// 암호화된 비밀번호와 SALT 설정
		request.setPassword(encryptedPassword);
		request.setSalt(salt);
		request.setRgtrId(request.getUserId());
		request.setMdfrId(request.getUserId());
		
		return authMapper.userInsert(UserEntity.toEntity(request));
	}
	
	/**
	 * 랜덤 SALT 생성
	 * @return Base64로 인코딩된 SALT 문자열
	 */
	private String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32]; // 256비트 SALT
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
	
	/**
	 * 비밀번호 암호화 (SHA-256 + SALT)
	 * @param password 원본 비밀번호
	 * @param salt SALT 값
	 * @return 암호화된 비밀번호 (Base64 인코딩)
	 */
	private String encryptPassword(String password, String salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			// 비밀번호에 SALT 추가
			String saltedPassword = password + salt;
			
			// SHA-256 해시 생성
			byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
			
			// Base64로 인코딩하여 반환
			return Base64.getEncoder().encodeToString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
		}
	}
}
