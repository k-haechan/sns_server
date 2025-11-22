package site.haechan.sns_backend.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.haechan.sns_backend.domain.auth.dto.request.LoginRequest;
import site.haechan.sns_backend.domain.auth.service.AuthService;
import site.haechan.sns_backend.domain.member.dto.response.MemberBriefResponse;
import site.haechan.sns_backend.domain.member.service.MemberService;
import site.haechan.sns_backend.global.common.response.ApiResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
	private final MemberService memberService;
	private final AuthService authService;

	@PostMapping( value = "/login", consumes = "application/json")
	@Operation(summary = "로그인", description = "회원 정보를 기반으로 로그인합니다.")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<MemberBriefResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		MemberBriefResponse loginResponse = memberService.login(request);
		authService.createAuthTokens(loginResponse.memberId(), response);
		return ApiResponse.success("로그인 성공", loginResponse);
	}

	@PostMapping(value = "logout")
	@Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.deleteAuthTokens(request, response);
		return ApiResponse.success("로그아웃이 성공적으로 완료되었습니다.");
	}
}
