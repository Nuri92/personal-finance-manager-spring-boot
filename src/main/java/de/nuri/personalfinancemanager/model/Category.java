package de.nuri.personalfinancemanager.model;

import java.util.Objects;

public class Category {
	private final Long id;
	private final String name;
	private final CategoryType type;

	public Category(String name, CategoryType type) {
		this(null, name, type);
	}

	private Category(Long id, String name, CategoryType type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public Category withId(Long id) {
		Objects.requireNonNull(id, "Category id must not be null");
		if (id <= 0) {
			throw new IllegalArgumentException("Category id must be positive");
		}

		return new Category(id, name, type);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public CategoryType getType() {
		return type;
	}
}
