package site.haechan.sns_backend.global.common.exeption;

import lombok.Getter;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;

@Getter
public class CustomException extends RuntimeException{
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
