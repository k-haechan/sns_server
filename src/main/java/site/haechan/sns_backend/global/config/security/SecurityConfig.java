package site.haechan.sns_backend.global.config.security;

import static org.apache.tomcat.util.http.Method.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import site.haechan.sns_backend.domain.auth.service.AuthService;
import site.haechan.sns_backend.global.config.security.exception.CustomAccessDeniedHandler;
import site.haechan.sns_backend.global.config.security.exception.CustomAuthenticationEntryPoint;
import site.haechan.sns_backend.global.config.security.filter.JwtAuthenticationFilter;
import site.haechan.sns_backend.global.cookie.CookieService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthService authService, CookieService cookieService) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 (JWT 방식)

			.authorizeHttpRequests(
				authorize -> authorize
					// Swagger 관련 URL 접근 허용
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
						"/swagger-ui.html").permitAll()
					// 에러 페이지 접근 허용
					.requestMatchers("/error").permitAll()

					// MEMBER Domain
					.requestMatchers(POST, "/api/v1/members/join").permitAll()
					.requestMatchers(POST, "/api/v1/auth/**").permitAll()
					.anyRequest().authenticated()
			)


			// ✅ JWT 필터 등록
			.addFilterBefore(
				new JwtAuthenticationFilter(authService, cookieService),
				UsernamePasswordAuthenticationFilter.class)

			// ✅ 기본 인증 방식 비활성화 (JWT 사용)
			.httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
			.formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화

			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				.accessDeniedHandler(new CustomAccessDeniedHandler())
			);


		return http.build();
	}
}
