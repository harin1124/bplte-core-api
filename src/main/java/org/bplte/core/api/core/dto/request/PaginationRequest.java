package org.bplte.core.api.core.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
	@Min(1)
	@Schema(description = "현재 페이지", defaultValue = "1")
	private int page;
	@Min(1)
	@Max(1000)
	@Schema(description = "페이지 개당 데이터 수", defaultValue = "10")
	private int size;
	@Hidden
	private int limit;
	@Hidden
	private int offset;
	
	public int getLimit() {
		return this.size;
	}
	
	public int getOffset() {
		return (this.page - 1) * this.size;
	}
}
