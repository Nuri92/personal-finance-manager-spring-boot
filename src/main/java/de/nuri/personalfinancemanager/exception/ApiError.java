package de.nuri.personalfinancemanager.exception;

import java.util.Map;

public record ApiError(
		String code,
		String message,
		Map<String, String> fieldErrors
) {
	public ApiError {
		fieldErrors = Map.copyOf(fieldErrors);
	}
}
