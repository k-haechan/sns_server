package site.haechan.sns_backend.global.jwt.component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;
import site.haechan.sns_backend.global.jwt.dto.JwtProperties;

@Component
public class JwtProvider {
	public String generateToken(Long memberId, JwtProperties properties) {
		SecretKey secretKey = properties.secretKey();
		Duration expiration = properties.expiration();

		Date now = new Date();
		Date expiry = Date.from(now.toInstant().plus(expiration));

		return Jwts.builder()
			.subject(memberId.toString())
			.issuedAt(now)
			.expiration(expiry)
			.signWith(secretKey)
			.compact();
	}

	public Claims parseClaims(String token, JwtProperties properties) {
		SecretKey secretKey = properties.secretKey();

		try {
			// JWT 파서 빌드 및 토큰 파싱
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		} catch (ExpiredJwtException e) {
			// 토큰 만료 시 처리
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);

		} catch (JwtException | IllegalArgumentException e) {
			// 유효하지 않은 토큰 처리
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	public boolean validateToken(String token, JwtProperties properties) {
		try {
			// 토큰 파싱 시도
			parseClaims(token, properties);
			// 만료 시간 확인
			return true;
		} catch (CustomException e) {
			return false;
		}
	}

	public Duration getLeftExpirationTime(String token, JwtProperties jwtProperties) {
		Instant expiration = parseClaims(token, jwtProperties).getExpiration().toInstant();
		Instant now = Instant.now();
		return Duration.between(now, expiration);
	}
}
