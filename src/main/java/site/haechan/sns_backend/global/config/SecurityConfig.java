package site.haechan.sns_backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
				authorize -> authorize
					// Swagger 관련 URL 접근 허용
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
						"/swagger-ui.html").permitAll()
					// 에러 페이지 접근 허용
					.requestMatchers("/error").permitAll()
					// 회원가입 API 접근 허용
					.requestMatchers("/api/v1/members/join").permitAll()
					.anyRequest().authenticated()
			);

		return http.build();
	}
}
