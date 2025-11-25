package site.haechan.sns_backend.domain.member.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.haechan.sns_backend.domain.auth.dto.request.LoginRequest;
import site.haechan.sns_backend.domain.member.dto.request.JoinRequest;
import site.haechan.sns_backend.domain.member.dto.response.MemberBriefResponse;
import site.haechan.sns_backend.domain.member.entity.Member;
import site.haechan.sns_backend.domain.member.repository.MemberRepository;
import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void join(JoinRequest request) {
		Member newMember = request.toEntity(passwordEncoder);
		try{
			memberRepository.save(newMember);
		} catch(DataIntegrityViolationException e) {
			String message = e.getMessage();

			if(message.contains("uc_member_email")) { // 자동생성된 제약조건 이름 사용(추후에 문제 생기면 @Table 어노테이션으로 이름 지정)
				throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
			}
			if (message.contains("uc_member_username")) {
				throw new CustomException(ErrorCode.USERNAME_DUPLICATE);
			}
			throw new CustomException(ErrorCode.DATABASE_ERROR);

		}
	}

	@Transactional(readOnly = true)
	public MemberBriefResponse login(LoginRequest request) {
		String username = request.username();
		String password = request.password();

		Member member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ErrorCode.BAD_CREDENTIAL);
		}
		return MemberBriefResponse.from(member);
	}
}
