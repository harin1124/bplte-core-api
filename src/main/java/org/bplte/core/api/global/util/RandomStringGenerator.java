package org.bplte.core.api.global.util;

import java.security.SecureRandom;

public class RandomStringGenerator {
	private static final String DEFAULT_CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	// 유틸 클래스 인스턴스화 방지
	private RandomStringGenerator() {}

	public static String generate(int maxLength) {
		validatePositive(maxLength, "maxLength");
		int length = SECURE_RANDOM.nextInt(maxLength) + 1; // 1..maxLength
		return generateWithCharset(length, DEFAULT_CHARSET);
	}

	/**
	 * 정확히 length 길이의 난수 문자열을 생성합니다.
	 *
	 * @param length 사이즈
	 * @return 난수 문자열
	 */
	public static String generateExactly(int length) {
		validatePositive(length, "length");
		return generateWithCharset(length, DEFAULT_CHARSET);
	}

	/**
	 * 커스텀 문자셋으로 정확히 length 길이의 난수 문자열을 생성합니다.
	 * 필요 시 숫자만/소문자만 정책 등에 활용 가능합니다.
	 */
	public static String generateExactly(int length, String charset) {
		validatePositive(length, "length");
		validateCharset(charset);
		return generateWithCharset(length, charset);
	}

	private static String generateWithCharset(int length, String charset) {
		StringBuilder sb = new StringBuilder(length);
		int bound = charset.length();
		for (int i = 0; i < length; i++) {
			int index = SECURE_RANDOM.nextInt(bound);
			sb.append(charset.charAt(index));
		}
		return sb.toString();
	}

	private static void validatePositive(int value, String paramName) {
		if (value < 1) {
			throw new IllegalArgumentException(paramName + " must be greater than 0.");
		}
	}

	private static void validateCharset(String charset) {
		if (charset == null || charset.isBlank()) {
			throw new IllegalArgumentException("charset must not be null or blank.");
		}
	}
}
