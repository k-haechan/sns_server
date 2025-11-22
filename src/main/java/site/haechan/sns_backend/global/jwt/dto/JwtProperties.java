package site.haechan.sns_backend.global.jwt.dto;

import java.time.Duration;

import javax.crypto.SecretKey;

public record JwtProperties(
	String tokenName,
	SecretKey secretKey,
	Duration expiration
) {}
