package site.haechan.sns_backend.domain.member.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.haechan.sns_backend.domain.member.dto.request.JoinRequest;
import site.haechan.sns_backend.domain.member.service.MemberService;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;

@DisplayName("MemberController 단위 테스트")
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MemberService memberService;

	@MockitoBean
	private MemberService mockMemberService;

	@Nested
	@DisplayName("회원가입")
	class JoinTests {
		@Test
		void 성공() throws Exception {
			// given
			JoinRequest joinRequest = new JoinRequest("testuser", "password123", "테스트 유저", "test@example.com");
			doNothing().when(memberService).join(any(JoinRequest.class));

			// when & then
			mockMvc.perform(post("/api/v1/members/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(joinRequest)))
				.andExpect(status().isCreated());
		}

		@Test
		void 실패_유효성검증() throws Exception {
			// given
			JoinRequest joinRequest = new JoinRequest("", "pwd", "", "invalid-email");

			// when & then
			mockMvc.perform(post("/api/v1/members/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(joinRequest)))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
		}
	}

}
