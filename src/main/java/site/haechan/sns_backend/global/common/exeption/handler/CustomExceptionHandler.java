package site.haechan.sns_backend.global.common.exeption.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import site.haechan.sns_backend.global.common.exeption.CustomException;
import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;
import site.haechan.sns_backend.global.common.response.ApiResponse;

@Slf4j
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
		log.error("{} 예외 발생: {}", errorCode.getHttpStatus(), errorCode.getMessage(), e);
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ApiResponse.error(errorCode));
	}
}
