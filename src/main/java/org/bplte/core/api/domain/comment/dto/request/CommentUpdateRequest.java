package org.bplte.core.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * [요청] 댓글 수정
 */
@Schema(name = "[요청] 댓글 수정 (CommentUpdateRequest)")
public record CommentUpdateRequest (
	@NotBlank
	@Schema(description = "내용", minLength = 1)
	String content
) {}
