package org.bplte.core.api.domain.user.entity;

import lombok.*;
import org.bplte.core.api.domain.auth.dto.request.AuthJoinRequest;

import java.time.LocalDateTime;


/**
 * 사용자
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	/** 사용자 아이디 */
	private String userId;
	/** 사용자 이름 */
	private String userName;
	/** 이메일 */
	private String email;
	/** SALT */
	private String salt;
	/** 패스워드 */
	private String password;
	/** 삭제 여부 */
	private String delYn;
	/** 등록 일시 */
	private LocalDateTime regDt;
	/** 등록자 아이디 */
	private String rgtrId;
	/** 수정 일시 */
	private LocalDateTime mdfcnDt;
	/** 수정자 아이디 */
	private String mdfrId;
	
	public static UserEntity toEntity(AuthJoinRequest request) {
		UserEntity entity = new UserEntity();
		entity.setUserId(request.getUserId());
		entity.setUserName(request.getUserName());
		entity.setEmail(request.getEmail());
		entity.setPassword(request.getPassword());
		entity.setSalt(request.getSalt());
		entity.setDelYn("N");
		entity.setRgtrId(request.getRgtrId());
		entity.setMdfrId(request.getMdfrId());
		return entity;
	}
}
