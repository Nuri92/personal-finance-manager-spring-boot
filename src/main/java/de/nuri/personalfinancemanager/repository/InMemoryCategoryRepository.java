package de.nuri.personalfinancemanager.repository;

import de.nuri.personalfinancemanager.exception.DuplicateCategoryException;
import de.nuri.personalfinancemanager.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {
	private final List<Category> storedCategories = new ArrayList<>();
	private long nextId = 1;

	@Override
	public synchronized Category save(Category category) {
		Objects.requireNonNull(category, "Category must not be null");

		boolean nameAlreadyExists = storedCategories.stream()
				.anyMatch(existingCategory ->
						existingCategory.getName().equalsIgnoreCase(category.getName()));
		if (nameAlreadyExists) {
			throw new DuplicateCategoryException("Category name already exists");
		}

		Category storedCategory = category.withId(nextId++);
		storedCategories.add(storedCategory);
		return storedCategory;
	}

	@Override
	public synchronized List<Category> findAll() {
		return new ArrayList<>(storedCategories);
	}

	@Override
	public synchronized Optional<Category> findById(Long id) {
		return storedCategories.stream()
				.filter(category -> Objects.equals(category.getId(), id))
				.findFirst();
	}
}
