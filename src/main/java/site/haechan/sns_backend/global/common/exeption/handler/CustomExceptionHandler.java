package site.haechan.sns_backend.global.common.exeption.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;
import site.haechan.sns_backend.global.common.response.ApiResponse;

@RestControllerAdvice
public class CustomExceptionHandler {
	/**
	 * CustomException 처리
	 */
	@ExceptionHandler(CustomException.class)
	ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ApiResponse.error(errorCode));
	}

	/**
	 * 모든 예외 처리 (예상치 못한 서버 오류)
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		// todo: 사용자 인증정보 객체 구현 및 로그용 DB 연결 후 로그 저장
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ApiResponse.of(errorCode.getMessage()));
	}
}
