package site.haechan.sns_backend.global.config.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.haechan.sns_backend.domain.auth.service.AuthService;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final AuthService authService;

	private void setAuthentication(Long memberId) {
		// 인증 객체 생성 및 SecurityContext에 저장
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			memberId,
			null,
			List.of(new SimpleGrantedAuthority("ROLE_USER"))
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}


	/**
	 * JWT 인증 필터: 요청마다 실행되며, access-token이 존재하면 사용자 인증을 수행합니다.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		try {
			Claims accessTokenClaims = authService.getClaimsFromAccessToken(request)
											.orElseThrow(()->new JwtException("Access token is missing"));
			// JWT 파싱 및 claims 추출
			Long memberId = Long.valueOf(accessTokenClaims.getSubject());
			setAuthentication(memberId);
		} catch (JwtException e) { // 토큰 검증 실패 시
			// refresh-token 검증
			Optional<Claims> refreshTokenClaims = authService.getClaimsFromRefreshToken(request);
			if (refreshTokenClaims.isPresent()) {
				// 리프레시 토큰이 유효하면 새로운 액세스 토큰 발급 로직 수행
				Long memberId = Long.valueOf(refreshTokenClaims.get().getSubject());
				// 새로운 액세스 토큰 생성 및 쿠키에 설정
				authService.createAuthTokens(memberId, response);
				// 인증 정보 설정
				setAuthentication(memberId);
			}
		}
		// 다음 필터로 요청 전달
		filterChain.doFilter(request, response);
	}
}
