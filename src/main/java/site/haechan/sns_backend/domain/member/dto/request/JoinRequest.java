package site.haechan.sns_backend.domain.member.dto.request;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.haechan.sns_backend.domain.member.entity.Member;

public record JoinRequest(
	@NotBlank(message = "username is required")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	@Schema(description = "회원가입할 사용자의 username", example = "testUser")
	String username,

	@NotBlank(message = "password is required")
	@Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
	@Schema(description = "회원가입할 사용자의 password", example = "password123")
	String password,

	@NotBlank(message = "realName is required")
	@Size(max = 50, message = "Real name must not exceed 50 characters")
	@Schema(description = "회원가입할 사용자의 realName", example = "테스트 유저")
	@JsonProperty("real-name")
	String realName,

	@NotBlank(message = "email is required")
	@Email(message = "Email should be valid")
	@Size(max = 100)
	@Schema(description = "회원가입할 사용자의 email", example = "testUser@email.com")
	String email
) {
	public Member toEntity(PasswordEncoder passwordEncoder) {
		return Member.create(username, passwordEncoder.encode(password), realName, email);
	}
}
