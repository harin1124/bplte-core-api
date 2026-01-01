package org.bplte.core.api.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthJoinRequest {
	private String userId;
	private String userName;
	private String email;
	private String password;
	private String salt;
	private String rgtrId;
	private String mdfrId;
}
