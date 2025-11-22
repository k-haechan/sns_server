package site.haechan.sns_backend.domain.auth.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.haechan.sns_backend.global.cache.RedisKeyType;
import site.haechan.sns_backend.global.cache.RedisService;
import site.haechan.sns_backend.global.cookie.CookieService;
import site.haechan.sns_backend.global.jwt.component.JwtProvider;
import site.haechan.sns_backend.global.jwt.dto.JwtProperties;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtProvider jwtProvider;
	private final CookieService cookieService;
	private final RedisService redisService;

	private final JwtProperties accessTokenProperties;
	private final JwtProperties refreshTokenProperties;

	public void createAuthTokens(Long memberId, HttpServletResponse response) {
		String accessToken = jwtProvider.generateToken(memberId, accessTokenProperties);
		cookieService.setCookie(response, accessTokenProperties.tokenName(), accessToken, accessTokenProperties.expiration());
		String refreshToken = jwtProvider.generateToken(memberId, refreshTokenProperties);
		cookieService.setCookie(response, refreshTokenProperties.tokenName(), refreshToken, refreshTokenProperties.expiration());
	}


	public void deleteAuthTokens(HttpServletRequest request, HttpServletResponse response) {
		// 쿠키에서 토큰 추출
		String refreshToken = cookieService.extractCookie(request, refreshTokenProperties.tokenName());
		// 리프레시 토큰을 블랙리스트에 추가하여 무효화
		redisService.set(
			RedisKeyType.BLACKLIST,
			refreshToken,
			"logout",
			jwtProvider.getLeftExpirationTime(refreshToken, refreshTokenProperties)
		);
		// 쿠키에서 액세스 토큰과 리프레시 토큰 제거
		cookieService.deleteCookie(response, accessTokenProperties.tokenName());

		SecurityContextHolder.clearContext();
	}

	public Optional<Claims> getClaimsFromAccessToken(HttpServletRequest request) {
		String accessToken = cookieService.extractCookie(request, accessTokenProperties.tokenName());
		if(accessToken == null)
			return Optional.empty();
		Claims claims = jwtProvider.parseClaims(accessToken, accessTokenProperties);
		return Optional.of(claims);
	}

	public Optional<Claims> getClaimsFromRefreshToken(HttpServletRequest request) {
		String refreshToken = cookieService.extractCookie(request, refreshTokenProperties.tokenName());
		if(refreshToken == null)
			return Optional.empty();
		Claims claims = jwtProvider.parseClaims(refreshToken, refreshTokenProperties);
		return Optional.of(claims);
	}
}
