package site.haechan.sns_backend.global.cookie;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.haechan.sns_backend.global.config.CookieConfig;

@Service
@RequiredArgsConstructor
public class CookieService {
	private final CookieConfig config;

	public void setCookie(HttpServletResponse response, String name, String value, Duration maxAge) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
			.httpOnly(true)             // 자바스크립트 접근 방지 (권장)
			.secure(true)               // 로컬 개발(HTTP)을 위해 false. 배포 시에는 true로 변경!
			.path("/")                  // 전체 경로에서 쿠키 접근 가능
			.maxAge(maxAge)             // Duration을 초로 자동 변환
			.domain(config.getDomain()) // 도메인 설정 추가
			.sameSite("Lax")            // SameSite 속성 설정
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	public String extractCookie(HttpServletRequest request, String name) {
		if (request.getCookies() == null)
			return null;

		for (Cookie cookie : request.getCookies()) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public void deleteCookie(HttpServletResponse response, String tokenName) {
		setCookie(response, tokenName, "", Duration.ofSeconds(0));
	}
}
