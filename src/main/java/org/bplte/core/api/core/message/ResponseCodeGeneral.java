package org.bplte.core.api.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeGeneral implements ResponseCodeInterface {
	SUCCESS("BPLTE200", "정상 처리되었습니다.", ""),
	BAD_REQUEST("BPLTE400", "잘못된 요청입니다.", "");
	
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
