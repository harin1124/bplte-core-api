package org.bplte.core.api.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [요청] 포스트 수정
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "[요청] 포스트 수정 (PostUpdateRequest)")
public class PostUpdateRequest {
	@Schema(description = "포스트 번호", hidden = true)
	private Long postNumber;
	@NotBlank
	@Size(min = 1, max = 100)
	@Schema(description = "제목", minLength = 1, maxLength = 100)
	private String title;
	@NotBlank
	@Schema(description = "내용", minLength = 1)
	private String content;
	@Schema(description = "검색 내용", hidden = true)
	private String searchContent;
	@Schema(description = "수정자 아이디", hidden = true)
	private String mdfrId;
	@Schema(description = "신규 첨부파일")
	private List<MultipartFile> addAttachFileList;
	@Schema(description = "삭제 첨부파일 (파일 아이디)")
	private List<Long> deleteFileIdList;
	@Schema(description = "첨부파일 저장 순서. 확장자 포함 파일명을 sort_order대로 나열 (신규 파일과 기존 파일 포함, 삭제 파일 제외)")
	private List<String> attachFileOrderList;
}
