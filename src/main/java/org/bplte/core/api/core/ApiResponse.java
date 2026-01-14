package org.bplte.core.api.core;

import lombok.*;
import org.bplte.core.api.core.message.ResponseCode;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.core.message.ResponseCodeInterface;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	private String resultCode;
	private String resultMessage;
	private String resultDetailMessage;
	private T data;
	
	public static <Void> ApiResponse<Void> success() {
		ResponseCode code = ResponseCodeGeneral.SUCCESS.getResponseCode();
		return ApiResponse.<Void>builder()
				.resultCode(code.getResultCode())
				.resultMessage(code.getResultMessage())
				.resultDetailMessage(code.getResultDetailMessage())
				.build();
	}
	
	public static <T> ApiResponse<T> success(T data) {
		ResponseCode code = ResponseCodeGeneral.SUCCESS.getResponseCode();
		return ApiResponse.<T>builder()
				.resultCode(code.getResultCode())
				.resultMessage(code.getResultMessage())
				.resultDetailMessage(code.getResultDetailMessage())
				.data(data)
				.build();
	}
	
	public static <T> ApiResponse<T> error(ResponseCodeInterface paramCode) {
		ResponseCode code = paramCode.getResponseCode();
		return ApiResponse.<T>builder()
				.resultCode(code.getResultCode())
				.resultMessage(code.getResultMessage())
				.resultDetailMessage(code.getResultDetailMessage())
				.build();
	}
}
