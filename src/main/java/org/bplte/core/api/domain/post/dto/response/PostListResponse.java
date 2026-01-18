package org.bplte.core.api.domain.post.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
	/** 포스트 번호 */
	private Long postNumber;
	/** 소유자 사용자 아이디 */
	private String ownerUserId;
	/** 제목 */
	private String title;
	/** 등록 일시 */
	private String regDt;
	/** 등록자 아이디 */
	private String rgtrId;
	/** 수정 일시 */
	private String mdfcnDt;
	/** 수정자 아이디 */
	private String mdfrId;
}
