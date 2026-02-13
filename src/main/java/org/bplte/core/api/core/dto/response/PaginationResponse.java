package org.bplte.core.api.core.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 페이지네이션된 목록 API 응답을 담는 DTO.
 * <p>
 * 페이지당 항목 수({@code itemSize}), 전체 항목 수({@code totalItemSize}), 현재 페이지 데이터({@code data})를 가지며,
 * 전체 페이지 수({@code pageSize})는 내부에서 자동 계산됩니다.
 *
 * @param <T> 목록 요소의 타입
 * @see #of(int, int, List)
 * @see #builder()
 */
@Getter
@Setter
public class PaginationResponse<T> {

	/** 페이지당 항목 수 */
	private int itemSize;

	/** 전체 페이지 수 (totalItemSize / itemSize 기준으로 계산) */
	private int pageSize;

	/** 전체 항목 수 */
	private int totalItemSize;

	/** 현재 페이지의 데이터 목록 */
	private List<T> data;

	/**
	 * 빌더로 페이지네이션 응답을 생성합니다.
	 * {@code pageSize}는 {@code itemSize}와 {@code totalItemSize}로 자동 계산됩니다.
	 *
	 * @param itemSize 페이지당 항목 수
	 * @param totalItemSize 전체 항목 수
	 * @param data 현재 페이지 데이터 목록
	 */
	@Builder
	public PaginationResponse(int itemSize, int totalItemSize, List<T> data) {
		this.itemSize = itemSize;
		this.totalItemSize = totalItemSize;
		this.data = data;
		this.pageSize = calculatePageSize(totalItemSize);
	}
	
	/**
	 * 페이지네이션 정보와 데이터 목록으로 {@link PaginationResponse}를 생성합니다.
	 * {@code pageSize}는 {@code totalItemSize}와 {@code itemSize}로 자동 계산됩니다.
	 *
	 * @param <T> 목록 요소의 타입
	 * @param itemSize 페이지당 항목 수
	 * @param totalItemSize 전체 항목 수
	 * @param data 현재 페이지 데이터 목록 (null이 아니어야 함)
	 * @return 생성된 {@link PaginationResponse}
	 *
	 * @see #builder()
	 */
	public static <T> PaginationResponse<T> of(int itemSize, int totalItemSize, List<T> data) {
		return PaginationResponse.<T>builder()
				.itemSize(itemSize)
				.totalItemSize(totalItemSize)
				.data(data)
				.build();
	}

	/**
	 * 전체 항목 수와 페이지당 항목 수로 전체 페이지 수를 계산합니다.
	 * itemSize가 1 미만이면 0을 반환합니다.
	 */
	private int calculatePageSize(int totalItemSize) {
		if (itemSize < 1) {
			return 0;
		}
		return (totalItemSize + itemSize - 1) / itemSize;
	}
}
