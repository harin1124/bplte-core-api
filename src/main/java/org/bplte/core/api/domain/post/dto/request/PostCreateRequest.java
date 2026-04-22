package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [요청] 포스트 등록
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 포스트 등록 (PostCreateRequest)")
public class PostCreateRequest {
	@NotBlank
	@Size(min = 1, max = 100)
	@Schema(description = "제목", minLength = 1, maxLength = 100)
	private String title;
	@NotBlank
	@Schema(description = "내용", minLength = 1)
	private String content;
	@Schema(hidden = true)
	private String searchContent;
	@Schema(hidden = true)
	private String requestUserId;
	@Schema(description = "첨부파일")
	private List<MultipartFile> attachFileList;
	@Schema(description = "첨부파일 저장 순서. 확장자 포함 파일명을 sort_order대로 나열")
	private List<String> attachFileOrderList;
}
