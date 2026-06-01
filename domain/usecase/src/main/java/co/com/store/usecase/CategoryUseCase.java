package co.com.store.usecase;

import org.springframework.stereotype.Service;
import co.com.store.model.Category;
import co.com.store.model.gateways.CategoryGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class CategoryUseCase extends BaseUseCase {

  private final CategoryGateway categoryGateway;

  public CategoryUseCase(CategoryGateway categoryGateway) {
    super();
    this.categoryGateway = categoryGateway;
  }

  public Mono<Category> createCategory(Category category) {
    validateNotNull(category, "La categoría no puede ser nula");
    validateNotEmpty(category.getName(), "El nombre de la categoría es requerido");
    validateNotEmpty(category.getDescription(), "La descripción de la categoría es requerida");

    LocalDateTime ahora = LocalDateTime.now();
    category.setCreatedDate(ahora);
    category.setUpdatedDate(ahora);

    return categoryGateway.save(category);
  }

  public Mono<Category> updateCategory(String id, Category category) {
    validateNotEmpty(id, "El ID de la categoría es requerido para actualizar");
    validateNotNull(category, "La categoría no puede ser nula");
    validateNotEmpty(category.getName(), "El nombre de la categoría es requerido");

    return categoryGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("La categoría con ID " + id + " no existe")))
        .flatMap(existingCategory -> {
          category.setId(existingCategory.getId());
          category.setCreatedDate(existingCategory.getCreatedDate());
          category.setUpdatedDate(LocalDateTime.now());
          return categoryGateway.update(category);
        });
  }

  public Mono<Category> getCategoryById(String id) {
    validateNotEmpty(id, "El ID de búsqueda es requerido");
    return categoryGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Categoría no encontrada")));
  }

  public Flux<Category> getAllCategories() {
    return categoryGateway.findAll();
  }

  public Mono<Void> deleteCategory(String id) {
    validateNotEmpty(id, "El ID es requerido para eliminar");
    return categoryGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("No se puede eliminar: Categoría no existe")))
        .flatMap(category -> categoryGateway.deleteById(id));
  }
}