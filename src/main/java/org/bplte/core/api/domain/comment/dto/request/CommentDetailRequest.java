package org.bplte.core.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * [요청] 댓글 조회
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 댓글 조회 (CommentDetailRequest)")
public class CommentDetailRequest {
	@Schema(description = "포스트 번호")
	private Long postNumber;
	@Schema(description = "댓글 번호 (고유값)")
	private Long commentId;
	@Schema(description = "요청 사용자 아이디")
	private String requestUserId;
}
