package site.haechan.sns_backend.global.jwt;

import java.time.Duration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JwtType {
	ACCESS("access-token", Duration.ofMinutes(30)),
	REFRESH("refresh-token", Duration.ofDays(14));

	private final String tokenName;
	private final Duration expiration;
}
