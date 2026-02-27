package org.bplte.core.api.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeGeneral implements ResponseCodeInterface {
	SUCCESS("BPLTE200", "정상 처리되었습니다.", ""),
	BAD_REQUEST("BPLTE400", "잘못된 요청입니다.", ""),
	NOT_FOUND("BPLTE404", "요청한 리소스를 찾을 수 없습니다.", ""),
	UNAUTHORIZED("BPLTE401", "인증이 필요합니다.", "로그인 후 다시 시도해주세요."),
	TOO_MANY_REQUESTS("BPLTE429", "요청이 너무 많습니다.", "10분당 호출 한도를 초과했습니다. 잠시 후 다시 시도해주세요."),
	UNKNOWN("BPLTE500", "알 수 없는 오류가 발생했습니다.", "");
	
	private final String resultCode;
	private final String resultMessage;
	private final String resultDetailMessage;
	
	@Override
	public ResponseCode getResponseCode() {
		return ResponseCode.builder()
				.resultCode(resultCode)
				.resultMessage(resultMessage)
				.resultDetailMessage(resultDetailMessage)
				.build();
	}
}
