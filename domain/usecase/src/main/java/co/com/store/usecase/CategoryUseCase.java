package co.com.store.usecase;

import co.com.store.model.Category;
import co.com.store.model.gateways.CategoryGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public class CategoryUseCase extends BaseUseCase {

  private final CategoryGateway categoryGateway;

  public CategoryUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  public Mono<Category> createCategory(Category category) {
    category.setCreatedDate(LocalDateTime.now());
    category.setUpdatedDate(LocalDateTime.now());
    return categoryGateway.save(category);
  }

  public Mono<Category> updateCategory(String id, Category category) {
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
    return categoryGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Categoría no encontrada")));
  }

  public Flux<Category> getAllCategories() {
    return categoryGateway.findAll();
  }

  public Mono<Void> deleteCategory(String id) {
    return categoryGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("No se puede eliminar: Categoría no existe")))
        .flatMap(category -> categoryGateway.deleteById(id));
  }
}