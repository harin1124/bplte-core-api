package org.bplte.core.api.domain.post.service;

import org.bplte.core.api.core.dto.response.PaginationResponse;
import org.bplte.core.api.domain.post.dto.request.PostListRequest;
import org.bplte.core.api.domain.post.dto.response.PostListResponse;
import org.bplte.core.api.domain.post.mapper.PostMapper;
import org.bplte.core.api.domain.post.service.impl.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PostServiceImpl.getPosts() 메소드 검증을 위한 단위 테스트.
 * Service 계층은 Mapper를 Mock하여 비즈니스 로직만 고립 검증한다.
 */
@ExtendWith(MockitoExtension.class)  // Mockito의 @Mock, @InjectMocks 사용 시 JUnit5와 연동되도록 필요
@DisplayName("PostServiceImpl getPosts 메소드")
class PostServiceImplTest {

	// Service가 의존하는 PostMapper를 Mock 객체로 둔다.
	// DB/MyBatis 호출 없이 Service 로직만 검증하기 위함
	@Mock
	private PostMapper postMapper;

	// PostServiceImpl 인스턴스 생성 시, 위 @Mock 필드를 자동 주입해준다.
	// 별도 생성자/빌더 없이 테스트 대상 객체를 준비할 수 있음
	@InjectMocks
	private PostServiceImpl postService;

	@Nested
	class 게시글이_존재할_때 {
		@Test
		void getPosts_검색_조건_전달_성공() {
			// given: 테스트 전제 조건 설정
			PostListRequest request = new PostListRequest();
			request.setPage(1);
			request.setSize(10);
			request.setTitleSearchKeyword("제목456");

			// Service 내부에서 totalCount 지정
			int totalCount = 3;
			List<PostListResponse> mockList = List.of(
				PostListResponse.builder().postNumber(1L).title("제목111").build(),
				PostListResponse.builder().postNumber(2L).title("제목222").build(),
				PostListResponse.builder().postNumber(3L).title("제목777").build()
			);

			// 반환값 고정
			when(postMapper.selectPostListCount()).thenReturn(totalCount);
			when(postMapper.selectPostList(any(PostListRequest.class))).thenReturn(mockList);

			// Captor 생성 (Mock 메소드가 어떤 인자로 호출되었는지 검증하기 위해 사용)
			ArgumentCaptor<PostListRequest> requestCaptor = ArgumentCaptor.forClass(PostListRequest.class);

			// when: 테스트 대상 메소드 실행
			PaginationResponse<PostListResponse> result = postService.getPosts(request);

			// verify 시, capture 로 이 호출에 전달된 인자를 캡처하도록 설정
			verify(postMapper).selectPostListCount(requestCaptor.capture());
			verify(postMapper).selectPostList(requestCaptor.capture());

			// 캡처된 인자 꺼내기
			// PostListRequest captured = requestCaptor.getValue();
		}

		@Test
		void getPosts_데이터있을때_조회_성공() {
			// given: 테스트 전제 조건 설정
			PostListRequest request = new PostListRequest();
			request.setPage(1); // 1페이지 요청
			request.setSize(10); // 페이지당 10건

			// Service 내부에서 totalCount > 0이면 selectPostList를 호출하므로, 0보다 큰 값으로 stub
			int totalCount = 2;
			List<PostListResponse> mockList = List.of(
				PostListResponse.builder().postNumber(1L).title("제목1").build(),
				PostListResponse.builder().postNumber(2L).title("제목2").build()
			);

			// Mapper 메소드 호출 시 반환값을 고정(stub). 실제 DB 호출 없이 원하는 값을 시뮬레이션
			when(postMapper.selectPostListCount()).thenReturn(totalCount);
			when(postMapper.selectPostList(any(PostListRequest.class))).thenReturn(mockList);

			// when: 테스트 대상 메소드 실행
			PaginationResponse<PostListResponse> result = postService.getPosts(request);

			// then: 기대 동작 검증
			// selectPostListCount가 정확히 1회 호출되었는지 확인 (Service 로직의 필수 호출 검증)
			verify(postMapper).selectPostListCount();
			// selectPostList가 PostListRequest를 인자로 1회 호출되었는지 확인
			verify(postMapper).selectPostList(any(PostListRequest.class));
			// PaginationResponse.of(request.getSize(), totalCount, postList) 호출 결과 검증
			assertThat(result.getItemSize()).isEqualTo(10);        // request.getSize()가 itemSize로 전달됨
			assertThat(result.getTotalItemSize()).isEqualTo(2);    // totalCount가 totalItemSize로 전달됨
			assertThat(result.getData()).hasSize(2);               // mockList 크기와 일치
			assertThat(result.getData()).extracting(PostListResponse::getTitle)
				.containsExactly("제목1", "제목2");  // Mapper가 반환한 목록이 그대로 data에 담기는지 검증
		}
	}

	@Nested
	class 게시글이_존재하지_않을_때 {
		@Test
		@DisplayName("selectPostList를 호출하지 않고 빈 목록으로 PaginationResponse를 반환한다")
		void getPosts_데이터없을때_selectPostList미호출_빈목록반환() {
			// === given ===
			PostListRequest request = new PostListRequest();
			request.setPage(1);
			request.setSize(10);
			// totalCount가 0이면 Service 내부 if(totalCount > 0) 분기에서 selectPostList를 호출하지 않음
			when(postMapper.selectPostListCount()).thenReturn(0);

			// === when ===
			PaginationResponse<PostListResponse> result = postService.getPosts(request);

			// === then ===
			verify(postMapper).selectPostListCount();
			// selectPostList가 호출되지 않아야 함. 불필요한 DB 쿼리 방지 로직 검증
			verify(postMapper, never()).selectPostList(any(PostListRequest.class));
			assertThat(result.getTotalItemSize()).isEqualTo(0);
			assertThat(result.getData()).isEmpty();  // new ArrayList<>(0) 또는 빈 리스트가 반환됨
			assertThat(result.getItemSize()).isEqualTo(10);
		}
	}
}
