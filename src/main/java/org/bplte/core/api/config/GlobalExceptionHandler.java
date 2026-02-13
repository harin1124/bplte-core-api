package org.bplte.core.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.core.exception.ApiException;
import org.bplte.core.api.core.message.ResponseCode;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.bplte.core.api.core.message.ResponseCodeInterface;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final Environment environment;
	@ExceptionHandler(value = {ApiException.class})
	protected ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
		ResponseCodeInterface code = ex.getResponseCode();
		ApiResponse<Object> response = ApiResponse.error(code);
		return ResponseEntity.ok(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String detail = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.collect(Collectors.joining(", "));
		ResponseCode code = ResponseCodeGeneral.BAD_REQUEST.getResponseCode();
		ApiResponse<Object> body = ApiResponse.builder()
				.resultCode(code.getResultCode())
				.resultMessage(code.getResultMessage())
				.resultDetailMessage(detail)
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
		ResponseCode code = ResponseCodeGeneral.UNKNOWN.getResponseCode();
		String detailMessage = code.getResultDetailMessage();
		if (isLocalProfile()) {
			detailMessage = ex.getClass().getSimpleName();
		}
		ApiResponse<Object> body = ApiResponse.builder()
				.resultCode(code.getResultCode())
				.resultMessage(code.getResultMessage())
				.resultDetailMessage(detailMessage)
				.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}

	private boolean isLocalProfile() {
		return Arrays.stream(environment.getActiveProfiles())
				.anyMatch(profile -> "local".equals(profile) || "dev".equals(profile));
	}
}
