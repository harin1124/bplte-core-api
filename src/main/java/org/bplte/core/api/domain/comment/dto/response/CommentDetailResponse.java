package org.bplte.core.api.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bplte.core.api.core.swagger.SwaggerConstant;

/**
 * [응답] 댓글 상세
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[응답] 댓글 상세 (CommentDetailResponse)")
public class CommentDetailResponse {
	@Schema(description = "댓글 번호 (고유값)")
	private Long commentId;

	@Schema(description = "포스트 번호")
	private Long postNumber;

	@Schema(description = "상위 댓글 아이디")
	private Long parentCommentId;

	@Schema(description = "루트 댓글 아이디")
	private Long rootCommentId;

	@Schema(description = "깊이")
	private int depth;

	@Schema(description = "내용")
	private String content;

	@Schema(description = "좋아요 수")
	private int likeCnt;

	@Schema(description = "싫어요 수")
	private int dislikeCnt;

	@Schema(description = "비밀 여부", format = SwaggerConstant.YN_DEFAULT, example = SwaggerConstant.YN_EXAMPLE)
	private String secretYn;

	@Schema(description = "삭제 여부", format = SwaggerConstant.YN_DEFAULT, example = SwaggerConstant.YN_EXAMPLE)
	private String delYn;

	@Schema(description = "등록 일시", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private String regDt;

	@Schema(description = "등록자 아이디", minLength = 1, maxLength = 30)
	private String rgtrId;

	@Schema(description = "등록자 명", minLength = 1, maxLength = 30)
	private String rgtrName;

	@Schema(description = "등록자 정보", format = SwaggerConstant.USER_INFO_DEFAULT, example = SwaggerConstant.USER_INFO_EXAMPLE)
	private String rgtrInfo;

	@Schema(description = "수정 일시", format = SwaggerConstant.DATE_DEFAULT, example = SwaggerConstant.DATE_EXAMPLE)
	private String mdfcnDt;

	@Schema(description = "수정자 아이디", minLength = 1, maxLength = 30)
	private String mdfrId;

	@Schema(description = "수정자 명", minLength = 1, maxLength = 30)
	private String mdfrName;

	@Schema(description = "수정자 정보", format = SwaggerConstant.USER_INFO_DEFAULT, example = SwaggerConstant.USER_INFO_EXAMPLE)
	private String mdfrInfo;
}
