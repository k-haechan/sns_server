package site.haechan.sns_backend.global.common.exeption.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// General Server Error
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
	INVALID_INPUT_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 입력 값입니다."),

	// Database Errors
	DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),

	// Member Errors
	EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
	USERNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 사용 중인 사용자 이름입니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
