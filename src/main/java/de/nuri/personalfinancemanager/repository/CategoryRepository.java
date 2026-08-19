package de.nuri.personalfinancemanager.repository;

import de.nuri.personalfinancemanager.model.Category;

import java.util.List;

public interface CategoryRepository {
	Category save(Category category);

	List<Category> findAll();
}
