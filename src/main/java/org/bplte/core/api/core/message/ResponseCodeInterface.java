package org.bplte.core.api.core.message;

public interface ResponseCodeInterface {
	ResponseCode getResponseCode();
	
	default String getResultCode() {
		return getResponseCode().getResultCode();
	}
}
