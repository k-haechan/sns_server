package site.haechan.sns_backend.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.haechan.sns_backend.domain.member.dto.request.JoinRequest;
import site.haechan.sns_backend.domain.member.service.MemberService;
import site.haechan.sns_backend.global.common.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {
	private final MemberService memberService;

	@PostMapping(value = "/join", consumes = "application/json")
	@Operation(summary = "회원 가입", description = "회원가입을 완료합니다.")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> join(@Valid @RequestBody JoinRequest request) {
		memberService.join(request);
		return ApiResponse.success("회원 가입이 완료되었습니다.");
	}
}
