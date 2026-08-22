package de.nuri.personalfinancemanager.dto;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.CategoryType;

public record CategoryResponse(Long id, String name, CategoryType type) {

	public static CategoryResponse from(Category category) {
		return new CategoryResponse(
				category.getId(),
				category.getName(),
				category.getType()
		);
	}
}
