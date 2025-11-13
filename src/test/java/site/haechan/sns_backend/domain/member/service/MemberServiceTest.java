package site.haechan.sns_backend.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import site.haechan.sns_backend.domain.member.dto.request.JoinRequest;
import site.haechan.sns_backend.domain.member.repository.MemberRepository;
import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;

@DisplayName("MemberService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberService memberService;

	@Nested
	@DisplayName("회원가입")
	class JoinTests {
		@Test
		void 성공() {
			// given
			JoinRequest joinRequest = new JoinRequest("testuser", "password123", "테스트 유저", "test@example.com");
			when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

			// when
			memberService.join(joinRequest);

			// then
			verify(memberRepository, times(1))
				.save(argThat(member -> member.getUsername().equals("testuser") && member.getPassword().equals("encodedPassword")));
		}

		@Test
		void 실패_중복된_사용자명() {
			// given
			JoinRequest joinRequest = new JoinRequest("existingUser", "password123", "테스트 유저", "test@example.com");
			when(memberRepository.save(any())).thenThrow(new org.springframework.dao.DataIntegrityViolationException("uc_member_username"));

			// when
			CustomException ex = assertThrows(CustomException.class, () -> memberService.join(joinRequest));

			// then
			assertEquals(ErrorCode.USERNAME_DUPLICATE, ex.getErrorCode());
		}

		@Test
		void 실패_중복된_이메일() {
			// given
			JoinRequest joinRequest = new JoinRequest("newUser", "password123", "테스트 유저", "exsiting@example.com");
			when(memberRepository.save(any())).thenThrow(
				new org.springframework.dao.DataIntegrityViolationException("uc_member_email"));

			// when
			CustomException ex = assertThrows(CustomException.class, () -> memberService.join(joinRequest));

			// then
			assertEquals(ErrorCode.EMAIL_DUPLICATE, ex.getErrorCode());
		}
	}
}
