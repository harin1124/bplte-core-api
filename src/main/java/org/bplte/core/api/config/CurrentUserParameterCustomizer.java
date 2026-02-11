package org.bplte.core.api.config;

import io.swagger.v3.oas.models.Operation;
import org.bplte.core.api.config.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Swagger(OpenAPI) 문서에서 @CurrentUser 파라미터를 노출하지 않도록 제거하는 커스터마이저.
 * 토큰에서 주입되는 값이므로 API 스펙에 포함되지 않아야 함.
 */
@Component
public class CurrentUserParameterCustomizer implements org.springdoc.core.customizers.OperationCustomizer {

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getParameters() == null) {
			return operation;
		}

		// 제거할 파라미터 인덱스 (OpenAPI 파라미터 순서 = 메서드 파라미터 순서 가정)
		Set<Integer> indicesToRemove = getCurrentUserParameterIndices(handlerMethod);
		if (indicesToRemove.isEmpty()) {
			return operation;
		}

		List<io.swagger.v3.oas.models.parameters.Parameter> params = new ArrayList<>(operation.getParameters());
		// 뒤 인덱스부터 제거해야 앞쪽 인덱스가 밀리지 않음
		indicesToRemove.stream()
				.sorted((a, b) -> Integer.compare(b, a))
				.forEach(idx -> {
					if (idx >= 0 && idx < params.size()) {
						params.remove(idx.intValue());
					}
				});
		operation.setParameters(params);
		return operation;
	}

	private Set<Integer> getCurrentUserParameterIndices(HandlerMethod handlerMethod) {
		Set<Integer> indices = new HashSet<>();
		MethodParameter[] arr = handlerMethod.getMethodParameters();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].hasParameterAnnotation(CurrentUser.class)) {
				indices.add(i);
			}
		}
		return indices;
	}
}
