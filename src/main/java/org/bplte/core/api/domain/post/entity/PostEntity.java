package org.bplte.core.api.domain.post.entity;

import lombok.*;
import org.bplte.core.api.domain.post.dto.request.PostCreateRequest;

import java.time.LocalDateTime;

/**
 * 포스트
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
	/** 포스트 번호 */
	private Long postNumber;
	/** 소유자 사용자 아이디 */
	private String ownerUserId;
	/** 제목 */
	private String title;
	/** 내용 */
	private String content;
	/** 검색 내용 */
	private String searchContent;
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
	
	public static PostEntity createToEntity(PostCreateRequest request) {
		return PostEntity.builder()
				.ownerUserId(request.getRequestUserId())
				.title(request.getTitle())
				.content(request.getContent())
				.searchContent(request.getSearchContent())
				.delYn("N")
				.rgtrId(request.getRequestUserId())
				.mdfrId(request.getRequestUserId())
				.build();
	}
}