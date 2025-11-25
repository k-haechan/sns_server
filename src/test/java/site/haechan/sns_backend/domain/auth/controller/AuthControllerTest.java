package site.haechan.sns_backend.domain.auth.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.haechan.sns_backend.domain.auth.dto.request.LoginRequest;
import site.haechan.sns_backend.domain.auth.service.AuthService;
import site.haechan.sns_backend.domain.member.dto.response.MemberBriefResponse;
import site.haechan.sns_backend.domain.member.service.MemberService;
import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;
import site.haechan.sns_backend.global.cookie.CookieService;

@DisplayName("AuthController 단위 테스트")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private AuthService authService;

	@MockitoBean
	private CookieService cookieService;


	@Nested
	@DisplayName("로그인")
	class LoginTests {
		@Test
		void 성공() throws Exception {
			// given
			LoginRequest loginRequest = new LoginRequest("testuser", "password123");

			// when
			when(memberService.login(loginRequest)).thenReturn(new MemberBriefResponse(1L, "testuser", "테스트 유저", "testUrl"));

			// then
			mockMvc.perform(post("/api/v1/auth/login")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.message").value("로그인 성공"));
		}

		@Test
		void 실패_존재하지_않는_회원정보() throws Exception {
			// given
			LoginRequest loginRequest = new LoginRequest("nonexistentuser", "password123");

			// when
			when(memberService.login(loginRequest)).thenThrow(new CustomException(ErrorCode.MEMBER_NOT_FOUND));

			// then
			mockMvc.perform(post("/api/v1/auth/login")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
		}

		@Test
		void 실패_잘못된_비밀번호() throws Exception {
			// given
			LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

			// when
			when(memberService.login(loginRequest)).thenThrow(new CustomException(ErrorCode.BAD_CREDENTIAL));

			// then
			mockMvc.perform(post("/api/v1/auth/login")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value(ErrorCode.BAD_CREDENTIAL.getMessage()));
		}
	}




}
