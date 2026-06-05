package org.bplte.core.api.domain.comment.swagger;

public class CommentApiDescription {
	public static final String GET_COMMENTS = """
		특정 댓글이나 특정 게시물의 댓글들을 조회합니다.
		
		비밀 댓글로 설정한 경우, 댓글 작성자가 아니면 내용이 보이지 않습니다.
		
		삭제한 댓글인 경우, 내용이 보이지 않습니다.
		""";

	public static final String CREATE_COMMENT = """
		댓글을 작성합니다.
		
		상위 댓글 아이디 (parentCommentId) : 특정 댓글의 하위 댓글인 경우, 특정 댓글의 아이디를 해당 필드에 지정해야 합니다.
		하위 댓글이 아닌 경우 null 로 지정합니다.
		
		루트 댓글 아이디 (rootCommentId) : 특정 댓글의 하위 댓글인 경우, 최상위 루트의 댓글 아이디를 해당 필드에 지정해야 합니다.
		하위 댓글이 아닌 경우 null 로 지정합니다.
		""";
}
