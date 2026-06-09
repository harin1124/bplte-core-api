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

	public static final String UPDATE_COMMENT = """
		댓글을 수정합니다.
		
		단, 아래와 같은 경우 수정이 불가능합니다.
		
		- 삭제된 댓글인 경우
		
		- 댓글 작성자가 아닌 경우
		
		- 존재하지 않는 게시글의 댓글인 경우
		
		- 댓글이 작성된 게시글이 삭제된 경우
		""";

	public static final String DELETE_COMMENT = """
		댓글을 삭제합니다.
		
		단, 아래와 같은 경우 삭제가 불가능합니다.
		
		- 댓글이 존재하지 않을 경우
		
		- 댓글 작성자가 아닌 경우
		
		- 존재하지 않는 게시글의 댓글인 경우
		""";

	public static final String REACTION_COMMENT = """
		댓글에 반응합니다.
		
		반응의 종류는 좋아요, 좋아요 취소, 싫어요, 싫어요 취소 입니다.
		
		만약 이미 좋아요를 누른 상태에서 싫어요를 누르면, 좋아요 기록은 없어집니다.
		
		반대의 경우도 마찬가지입니다.
		""";
}
