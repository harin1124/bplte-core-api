package org.bplte.core.api.core.exception;

import lombok.Getter;
import org.bplte.core.api.core.message.ResponseCodeInterface;

@Getter
public class ApiException extends RuntimeException {
	private final ResponseCodeInterface responseCode;
	
	public ApiException(ResponseCodeInterface code) {
		this.responseCode = code;
	}
}
