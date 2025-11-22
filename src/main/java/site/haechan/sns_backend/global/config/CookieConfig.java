package site.haechan.sns_backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class CookieConfig {

	@Getter
	@Value("${custom.cookie.domain}")
	private String domain;
}
