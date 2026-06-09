package org.bplte.core.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.bplte.core.api.core.swagger.SwaggerConstant;

/**
 * [요청] 댓글 등록
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 댓글 등록 (CommentCreateRequest)")
public class CommentCreateRequest {
	@NotBlank
	@Schema(description = "포스트 번호")
	private Long postNumber;
	@NotBlank
	@Schema(description = "내용", minLength = 1)
	private String content;
	@Schema(description = "상위 댓글 아이디")
	private Long parentCommentId;
	@Schema(description = "루트 댓글 아이디")
	private Long rootCommentId;
	@NotBlank
	@Schema(description = "댓글 깊이")
	private int depth;
	@NotBlank
	@Pattern(regexp = SwaggerConstant.YN_DEFAULT)
	@Schema(description = "비밀 여부", format = SwaggerConstant.YN_DEFAULT, example = SwaggerConstant.YN_EXAMPLE)
	private String secretYn;
}
