package org.bplte.core.api.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bplte.core.api.core.ApiResponse;
import org.bplte.core.api.core.message.ResponseCodeGeneral;
import org.jspecify.annotations.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 특정 경로에 대해 IP 단위 호출 횟수 제한을 적용하는 필터
 *
 * <p>
 * 토큰 버킷 알고리즘(Bucket4j)으로 한도를 적용하고, IP별 버킷은 인메모리 Caffeine 캐시에 보관한다.
 * 오래 사용되지 않는 IP의 버킷은 만료되어 메모리 사용량이 제한된다.
 * </p>
 * <p>
 * local, dev 프로필에서는 제한을 적용하지 않고 통과시킨다.
 * </p>
 */
@Slf4j
@Component
@Order(1)
public class AuthAvailableUserIdRateLimitFilter extends OncePerRequestFilter {
	private static final String PATH_AVAILABLE_USER_ID = "/auth/available/user_id";
	private static final int CAPACITY = 10;
	private static final Duration REFILL_PERIOD = Duration.ofMinutes(10);
	private static final int CACHE_MAX_SIZE = 10_000;
	private static final long CACHE_EXPIRE_MINUTES = 15;

	private final Environment environment;
	private final Cache<String, Bucket> bucketCache;

	public AuthAvailableUserIdRateLimitFilter(Environment environment) {
		this.environment = environment;
		this.bucketCache = Caffeine.newBuilder()
			.maximumSize(CACHE_MAX_SIZE)
			.expireAfterAccess(Duration.ofMinutes(CACHE_EXPIRE_MINUTES))
			.build();
	}

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
		if (environment.acceptsProfiles(Profiles.of("local"))) {
			return true;
		}
		String fullPath = request.getContextPath() + PATH_AVAILABLE_USER_ID;
		return !("GET".equalsIgnoreCase(request.getMethod()) && fullPath.equals(request.getRequestURI()));
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		String clientKey = resolveClientKey(request);
		Bucket bucket = bucketCache.get(clientKey, k -> createBucket());

		var probe = bucket.tryConsumeAndReturnRemaining(1);
		if (probe.isConsumed()) {
			filterChain.doFilter(request, response);
			return;
		}

		log.warn("Rate limit exceeded for {}, clientKey: {}", request.getContextPath() + PATH_AVAILABLE_USER_ID, clientKey);

		response.setStatus(429);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		long secondsToWait = Math.max(1, TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
		response.setHeader("Retry-After", String.valueOf(secondsToWait));
		ApiResponse<Object> errorResponse = ApiResponse.error(ResponseCodeGeneral.TOO_MANY_REQUESTS);
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}

	private static String resolveClientKey(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (forwarded != null && !forwarded.isBlank()) {
			int comma = forwarded.indexOf(',');
			String first = comma > 0 ? forwarded.substring(0, comma).trim() : forwarded.trim();
			if (!first.isBlank()) {
				return first;
			}
		}
		String realIp = request.getHeader("X-Real-IP");
		if (realIp != null && !realIp.isBlank()) {
			return realIp.trim();
		}
		String remote = request.getRemoteAddr();
		return remote != null ? remote : "unknown";
	}

	private static Bucket createBucket() {
		return Bucket.builder()
			.addLimit(limit -> limit.capacity(CAPACITY).refillGreedy(CAPACITY, REFILL_PERIOD))
			.build();
	}
}
