package site.haechan.sns_backend.global.common.response;

import site.haechan.sns_backend.global.common.exeption.error.ErrorCode;

public record ApiResponse<T> (
	String message,
	T data
) {
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(message, data);
	}

	public static ApiResponse<Void> success(String message) {
		return new ApiResponse<>(message, null);
	}

	public static ApiResponse<Void> error(ErrorCode errorCode) {
		return new ApiResponse<>(errorCode.getMessage(), null);
	}

	public static <T> ApiResponse<T> error(ErrorCode errorCode, T data) {
		return new ApiResponse<>(errorCode.getMessage(), data);
	}
}
