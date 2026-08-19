package de.nuri.personalfinancemanager.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

	@Test
	void shouldCreateCopyWithId() {
		Category category = new Category("Groceries", CategoryType.EXPENSE);

		Category categoryWithId = category.withId(1L);

		assertThat(category.getId()).isNull();
		assertThat(categoryWithId.getId()).isEqualTo(1L);
		assertThat(categoryWithId.getName()).isEqualTo("Groceries");
		assertThat(categoryWithId.getType()).isEqualTo(CategoryType.EXPENSE);
	}

	@Test
	void shouldRejectNullId() {
		Category category = new Category("Groceries", CategoryType.EXPENSE);

		assertThatNullPointerException()
				.isThrownBy(() -> category.withId(null))
				.withMessage("Category id must not be null");
	}

	@Test
	void shouldRejectNonPositiveId() {
		Category category = new Category("Groceries", CategoryType.EXPENSE);

		assertThatThrownBy(() -> category.withId(0L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category id must be positive");
		assertThatThrownBy(() -> category.withId(-1L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Category id must be positive");
	}
}
