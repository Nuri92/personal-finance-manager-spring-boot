package de.nuri.personalfinancemanager.controller;

import de.nuri.personalfinancemanager.exception.DuplicateCategoryException;
import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.CategoryType;
import de.nuri.personalfinancemanager.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService categoryService;

	@Test
	void shouldCreateCategory() throws Exception {
		Category savedCategory =
				new Category("Groceries", CategoryType.EXPENSE).withId(1L);
		when(categoryService.addCategory(any(Category.class))).thenReturn(savedCategory);

		mockMvc.perform(post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Groceries",
								  "type": "EXPENSE"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Groceries"))
				.andExpect(jsonPath("$.type").value("EXPENSE"));
	}

	@Test
	void shouldReturnAllCategories() throws Exception {
		when(categoryService.getCategories()).thenReturn(List.of(
				new Category("Groceries", CategoryType.EXPENSE).withId(1L),
				new Category("Salary", CategoryType.INCOME).withId(2L)
		));

		mockMvc.perform(get("/api/categories"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Groceries"))
				.andExpect(jsonPath("$[0].type").value("EXPENSE"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value("Salary"))
				.andExpect(jsonPath("$[1].type").value("INCOME"));
	}

	@Test
	void shouldRejectBlankCategoryName() throws Exception {
		mockMvc.perform(post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "   ",
								  "type": "EXPENSE"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.message").value("Request validation failed"))
				.andExpect(jsonPath("$.fieldErrors.name")
						.value("Category name must not be blank"));
	}

	@Test
	void shouldRejectMissingCategoryType() throws Exception {
		mockMvc.perform(post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Groceries"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.fieldErrors.type")
						.value("Category type must not be null"));
	}

	@Test
	void shouldRejectInvalidCategoryType() throws Exception {
		mockMvc.perform(post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Groceries",
								  "type": "UNKNOWN"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("MALFORMED_REQUEST"))
				.andExpect(jsonPath("$.message").value("Request body is invalid"));
	}

	@Test
	void shouldReturnConflictForDuplicateCategory() throws Exception {
		when(categoryService.addCategory(any(Category.class)))
				.thenThrow(new DuplicateCategoryException("Category name already exists"));

		mockMvc.perform(post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Groceries",
								  "type": "EXPENSE"
								}
								"""))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.code").value("CATEGORY_ALREADY_EXISTS"))
				.andExpect(jsonPath("$.message").value("Category name already exists"));
	}
}
