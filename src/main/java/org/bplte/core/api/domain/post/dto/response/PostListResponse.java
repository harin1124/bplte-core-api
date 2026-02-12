package org.bplte.core.api.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bplte.core.api.core.swagger.SwaggerConstant;

/**
 * [응답] 포스트 목록
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 포스트 목록 (PostListResponse)")
public class PostListResponse {
	@Schema(description = "포스트 번호 (고유값)")
	private Long postNumber;
	
	@Schema(description = "소유자 사용자 아이디")
	private String ownerUserId;
	
	@Schema(description = "제목", minLength = 1, maxLength = 100)
	private String title;
	
	@Schema(description = "등록 일시", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private String regDt;
	
	@Schema(description = "등록자 아이디", minLength = 1, maxLength = 30)
	private String rgtrId;
	
	@Schema(description = "등록자 명", minLength = 1, maxLength = 30)
	private String rgtrName;
	
	@Schema(description = "등록자 정보", format = "등록자 명(등록자 아이디)")
	private String rgtrInfo;
	
	@Schema(description = "수정 일시", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private String mdfcnDt;
	
	@Schema(description = "수정자 아이디", minLength = 1, maxLength = 30)
	private String mdfrId;
	
	@Schema(description = "수정자 명", minLength = 1, maxLength = 30)
	private String mdfrName;
	
	@Schema(description = "수정자 정보", format = "수정자 명(수정자 아이디)")
	private String mdfrInfo;
}
