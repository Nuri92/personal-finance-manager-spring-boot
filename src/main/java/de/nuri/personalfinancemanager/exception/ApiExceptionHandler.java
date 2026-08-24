package de.nuri.personalfinancemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(fieldError ->
				fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));

		ApiError error = new ApiError(
				"VALIDATION_ERROR",
				"Request validation failed",
				fieldErrors
		);
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleUnreadableMessage() {
		ApiError error = new ApiError(
				"MALFORMED_REQUEST",
				"Request body is invalid",
				Map.of()
		);
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(DuplicateCategoryException.class)
	public ResponseEntity<ApiError> handleDuplicateCategory(DuplicateCategoryException exception) {
		ApiError error = new ApiError(
				"CATEGORY_ALREADY_EXISTS",
				exception.getMessage(),
				Map.of()
		);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException exception) {
		ApiError error = new ApiError(
				"CATEGORY_NOT_FOUND",
				exception.getMessage(),
				Map.of()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}
