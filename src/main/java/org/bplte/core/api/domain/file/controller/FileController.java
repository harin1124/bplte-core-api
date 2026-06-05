package org.bplte.core.api.domain.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bplte.core.api.core.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "파일")
public class FileController {
	@GetMapping
	@Operation(summary = "파일 다운로드")
	public ApiResponse<Void> g(){
		return null;
	}
}
