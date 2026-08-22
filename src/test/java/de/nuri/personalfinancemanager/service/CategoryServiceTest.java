package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.exception.DuplicateCategoryException;
import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.CategoryType;
import de.nuri.personalfinancemanager.repository.InMemoryCategoryRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryServiceTest {

	private final CategoryService service =
			new CategoryService(new InMemoryCategoryRepository());

	@Test
	void shouldAddCategory() {
		Category savedCategory = service.addCategory(
				new Category("Groceries", CategoryType.EXPENSE));

		assertThat(savedCategory.getId()).isEqualTo(1L);
		assertThat(service.getCategories()).containsExactly(savedCategory);
	}

	@Test
	void shouldRejectBlankName() {
		Category category = new Category("   ", CategoryType.EXPENSE);

		assertThatThrownBy(() -> service.addCategory(category))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category name must not be blank");
	}

	@Test
	void shouldRejectMissingType() {
		Category category = new Category("Groceries", null);

		assertThatThrownBy(() -> service.addCategory(category))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category type must not be null");
	}

	@Test
	void shouldRejectDuplicateNameIgnoringCase() {
		service.addCategory(new Category("Groceries", CategoryType.EXPENSE));

		assertThatThrownBy(() -> service.addCategory(
				new Category("groceries", CategoryType.EXPENSE)))
				.isInstanceOf(DuplicateCategoryException.class)
				.hasMessage("Category name already exists");
	}

	@Test
	void shouldTrimCategoryName() {
		Category savedCategory = service.addCategory(
				new Category("  Groceries  ", CategoryType.EXPENSE));

		assertThat(savedCategory.getName()).isEqualTo("Groceries");
	}

	@Test
	void shouldRejectDuplicateNameWithSurroundingWhitespace() {
		service.addCategory(new Category("Groceries", CategoryType.EXPENSE));

		assertThatThrownBy(() -> service.addCategory(
				new Category("  groceries  ", CategoryType.EXPENSE)))
				.isInstanceOf(DuplicateCategoryException.class)
				.hasMessage("Category name already exists");
	}
}
