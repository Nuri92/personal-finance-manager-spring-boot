package de.nuri.personalfinancemanager.repository;

import de.nuri.personalfinancemanager.exception.DuplicateCategoryException;
import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.CategoryType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryCategoryRepositoryTest {

	@Test
	void shouldAssignIdAndStoreCategory() {
		InMemoryCategoryRepository repository = new InMemoryCategoryRepository();

		Category savedCategory = repository.save(new Category("Groceries", CategoryType.EXPENSE));

		assertThat(savedCategory.getId()).isEqualTo(1L);
		assertThat(repository.findAll()).containsExactly(savedCategory);
	}

	@Test
	void shouldReturnCopyOfStoredCategories() {
		InMemoryCategoryRepository repository = new InMemoryCategoryRepository();
		repository.save(new Category("Salary", CategoryType.INCOME));

		List<Category> returnedCategories = repository.findAll();
		returnedCategories.clear();

		assertThat(repository.findAll()).hasSize(1);
	}

	@Test
	void shouldRejectDuplicateNameIgnoringCase() {
		InMemoryCategoryRepository repository = new InMemoryCategoryRepository();
		repository.save(new Category("Groceries", CategoryType.EXPENSE));

		assertThatThrownBy(() -> repository.save(
				new Category("groceries", CategoryType.EXPENSE)))
				.isInstanceOf(DuplicateCategoryException.class)
				.hasMessage("Category name already exists");
	}
}
