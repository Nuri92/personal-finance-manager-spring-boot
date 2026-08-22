package de.nuri.personalfinancemanager.dto;

import de.nuri.personalfinancemanager.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
		@NotBlank(message = "Category name must not be blank")
		String name,
		@NotNull(message = "Category type must not be null")
		CategoryType type
) {
}
