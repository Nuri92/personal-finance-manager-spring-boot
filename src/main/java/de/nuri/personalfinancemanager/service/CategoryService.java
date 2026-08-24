package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.exception.CategoryNotFoundException;
import de.nuri.personalfinancemanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
	private final CategoryRepository repository;

	public CategoryService(CategoryRepository repository) {
		this.repository = Objects.requireNonNull(repository, "Repository must not be null");
	}

	public Category addCategory(Category category) {
		Objects.requireNonNull(category, "Category must not be null");

		if (category.getName() == null || category.getName().isBlank()) {
			throw new IllegalArgumentException("Category name must not be blank");
		}
		if (category.getType() == null) {
			throw new IllegalArgumentException("Category type must not be null");
		}

		Category normalizedCategory =
				new Category(category.getName().trim(), category.getType());
		return repository.save(normalizedCategory);
	}

	public List<Category> getCategories() {
		return repository.findAll();
	}

	public Category getCategory(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException(id));
	}
}
