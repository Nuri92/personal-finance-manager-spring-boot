package de.nuri.personalfinancemanager.controller;

import de.nuri.personalfinancemanager.dto.CategoryResponse;
import de.nuri.personalfinancemanager.dto.CreateCategoryRequest;
import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 *Die Klasse gehört zur Controller-Schicht. Sie ist für HTTP-Anfragen und HTTP-Antworten zuständig.
 * Importiert werden unter anderem:
 * CreateCategoryRequest
 * CategoryResponse
 * Category
 * CategoryService
 * Damit verwendet der Controller drei unterschiedliche Datenformen:
 * CreateCategoryRequest → eingehende API-Daten
 * Category              → internes Modell
 * CategoryResponse      → ausgehende API-Daten
 * Controller-Annotationen
 * @RestController
 * @RequestMapping("/api/categories")
 *
 * @RestController registriert die Klasse als REST-Controller.
 * @RequestMapping("/api/categories") definiert den gemeinsamen Basispfad aller Methoden dieser Klasse.
 *
 * Spring erkennt, dass der Controller einen CategoryService benötigt. Da der Service mit @Service registriert ist, erstellt Spring ihn und übergibt ihn an den Constructor.
 * Das ist wieder Constructor Injection:
 * Spring
 *   |
 *   | erzeugt und übergibt
 *   v
 * CategoryService
 *   |
 *   v
 * CategoryController
 * Der Controller erstellt den Service bewusst nicht selbst.
 * */

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping
	public ResponseEntity<CategoryResponse> createCategory(
			@Valid @RequestBody CreateCategoryRequest request) {
		Category category = new Category(request.name(), request.type());
		Category savedCategory = categoryService.addCategory(category);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(CategoryResponse.from(savedCategory));
	}

	@GetMapping
	public List<CategoryResponse> getCategories() {
		return categoryService.getCategories().stream()
				.map(CategoryResponse::from)
				.toList();
	}
}
