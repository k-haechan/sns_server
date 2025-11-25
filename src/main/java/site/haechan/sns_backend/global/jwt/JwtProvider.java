package site.haechan.sns_backend.global.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;

@Component
public class JwtProvider {

	private final SecretKey accessTokenSecretKey;
	private final SecretKey refreshTokenSecretKey;

	public JwtProvider(
		@Value("${custom.jwt.access-token.secret}") String accessTokenSecret,
		@Value("${custom.jwt.refresh-token.secret}") String refreshTokenSecret
	) {
		this.accessTokenSecretKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
		this.refreshTokenSecretKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));
	}




	public String generateToken(Long memberId, JwtType jwtType) {
		Duration expiration = jwtType.getExpiration();
		SecretKey secretKey = jwtType.equals(JwtType.ACCESS) ? accessTokenSecretKey : refreshTokenSecretKey;

		Date now = new Date();
		Date expiry = Date.from(now.toInstant().plus(expiration));

		return Jwts.builder()
			.subject(memberId.toString())
			.issuedAt(now)
			.expiration(expiry)
			.signWith(secretKey)
			.compact();
	}

	public Claims parseClaims(String token, JwtType jwtType) throws CustomException {
		SecretKey secretKey = jwtType.equals(JwtType.ACCESS) ? accessTokenSecretKey : refreshTokenSecretKey;

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

	public Duration getLeftExpirationTime(String token, JwtType jwtType) {
		Instant expiration = parseClaims(token, jwtType).getExpiration().toInstant();
		Instant now = Instant.now();
		return Duration.between(now, expiration);
	}
}
