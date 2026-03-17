package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bplte.core.api.core.dto.request.PaginationRequest;
import org.bplte.core.api.core.enums.SortType;

/**
 * [요청] 포스트 조회
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 포스트 조회 (PostListRequest)")
public class PostListRequest extends PaginationRequest {
	@Schema(description = "제목 검색 키워드")
	private String titleSearchKeyword;
	@Schema(description = "등록자 아이디 검색 키워드")
	private String rgtrIdSearchKeyword;
	@Schema(description = "등록자 이름 검색 키워드")
	private String rgtrNameSearchKeyword;
	@Schema(description = "정렬 컬럼 이름")
	private String sortColumnName;
	@Schema(description = "정렬 타입")
	private SortType sortType;
}
