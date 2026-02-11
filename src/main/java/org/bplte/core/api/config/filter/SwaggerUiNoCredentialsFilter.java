package org.bplte.core.api.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Swagger UI index HTML 응답에 스크립트를 주입하여, "Try it out" 요청 시
 * 브라우저가 Cookie(HttpOnly 등)를 보내지 않도록 한다.
 * <p>
 * 프론트가 AccessToken을 HttpOnly 쿠키로 보내는 경우, 같은 origin에서
 * Swagger UI를 쓰면 Authorize 로그아웃 후에도 쿠키가 전송되는 문제를 막기 위해
 * 페이지 내 모든 fetch에 {@code credentials: 'omit'}을 적용한다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class SwaggerUiNoCredentialsFilter extends OncePerRequestFilter {

	private static final String FETCH_OMIT_SCRIPT =
		"<script>(function(){var f=window.fetch;window.fetch=function(u,o){return f.call(this,u,Object.assign({},o||{},{credentials:'omit'}));};})();</script>";

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		// swagger-ui index 페이지만 처리 (정적 자원 제외)
		boolean isIndex = path.contains("swagger-ui") && (
			path.endsWith("index.html") || path.endsWith("swagger-ui") || path.endsWith("swagger-ui/"));
		return !isIndex;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		CharResponseWrapper wrapper = new CharResponseWrapper(response);
		filterChain.doFilter(request, wrapper);

		String contentType = response.getContentType();
		if (contentType == null || !contentType.toLowerCase().contains("text/html")) {
			return;
		}

		String html = wrapper.getCapture();
		if (html == null || html.isEmpty()) {
			return;
		}

		String modified = injectScript(html);
		byte[] bytes = modified.getBytes(StandardCharsets.UTF_8);
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
	}

	private String injectScript(String html) {
		int headEnd = html.indexOf("</head>");
		if (headEnd == -1) {
			return html + FETCH_OMIT_SCRIPT;
		}
		return html.substring(0, headEnd) + FETCH_OMIT_SCRIPT + html.substring(headEnd);
	}

	private static final class CharResponseWrapper extends jakarta.servlet.http.HttpServletResponseWrapper {
		private final ByteArrayOutputStream capture = new ByteArrayOutputStream();
		private PrintWriter writer;

		CharResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public void flushBuffer() throws IOException {
			if (writer != null) {
				writer.flush();
			}
			capture.flush();
		}

		@Override
		public jakarta.servlet.ServletOutputStream getOutputStream() {
			return new jakarta.servlet.ServletOutputStream() {
				@Override
				public boolean isReady() { return true; }
				@Override
				public void setWriteListener(jakarta.servlet.WriteListener listener) { }
				@Override
				public void write(int b) { capture.write(b); }
				@Override
				public void write(byte[] b, int off, int len) { capture.write(b, off, len); }
			};
		}

		@Override
		public PrintWriter getWriter() {
			if (writer == null) {
				writer = new PrintWriter(new OutputStreamWriter(capture, StandardCharsets.UTF_8));
			}
			return writer;
		}

		String getCapture() throws IOException {
			if (writer != null) {
				writer.flush();
			}
			return capture.toString(StandardCharsets.UTF_8);
		}
	}
}
