package site.haechan.sns_backend.domain.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import site.haechan.sns_backend.global.cache.RedisKeyType;
import site.haechan.sns_backend.global.cache.RedisService;
import site.haechan.sns_backend.global.jwt.JwtProvider;
import site.haechan.sns_backend.global.jwt.JwtType;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtProvider jwtProvider;
	private final RedisService redisService;

	public String generateToken(Long memberId, JwtType type) {
		return jwtProvider.generateToken(memberId, type);
	}


	public void revokeRefreshToken(String refreshToken) {
		// 리프레시 토큰을 블랙리스트에 추가하여 무효화
		redisService.set(
			RedisKeyType.BLACKLIST,
			refreshToken,
			"logout",
			jwtProvider.getLeftExpirationTime(refreshToken, JwtType.REFRESH)
		);

	}

	public Optional<Claims> validateToken(String token, JwtType type) {
		if (token == null || type == JwtType.ACCESS && redisService.hasKey(RedisKeyType.BLACKLIST, token))
			return Optional.empty();

		try {
			Claims claims = jwtProvider.parseClaims(token, type);
			return Optional.of(claims);
		} catch (Exception e) {
			return Optional.empty();
		}
	}


}
