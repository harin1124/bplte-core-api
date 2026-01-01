package org.bplte.core.api.core.message;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ResponseCode {
	private final String resultCode;
	private final String resultMessage;
	private final String resultDetailMessage;
}
