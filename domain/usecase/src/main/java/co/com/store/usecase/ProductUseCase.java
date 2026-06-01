package co.com.store.usecase;

import org.springframework.stereotype.Service;
import co.com.store.model.Product;
import co.com.store.model.gateways.ProductGateway;
import co.com.store.model.gateways.StoreGateway;
import co.com.store.model.gateways.CategoryGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class ProductUseCase extends BaseUseCase {

  private final ProductGateway productGateway;
  private final StoreGateway storeGateway;
  private final CategoryGateway categoryGateway;

  public ProductUseCase(ProductGateway productGateway, StoreGateway storeGateway, CategoryGateway categoryGateway) {
    super();
    this.productGateway = productGateway;
    this.storeGateway = storeGateway;
    this.categoryGateway = categoryGateway;
  }

  public Mono<Product> createProduct(Product product) {
    validateNotNull(product, "El producto no puede ser nulo");
    validateNotEmpty(product.getName(), "El nombre del producto es requerido");
    validateNotNull(product.getPrice(), "El precio del producto es requerido");
    validate(product.getPrice() > 0, "El precio del producto debe ser mayor a 0");
    validateNotEmpty(product.getStoreId(), "El ID de la tienda (storeId) es requerido");
    validateNotEmpty(product.getCategoryId(), "El ID de la categoría (categoryId) es requerido");

    LocalDateTime ahora = LocalDateTime.now();
    product.setCreatedDate(ahora);
    product.setUpdatedDate(ahora);

    return storeGateway.findById(product.getStoreId())
        .switchIfEmpty(Mono.error(new IllegalArgumentException("La tienda asociada no existe")))
        .flatMap(store -> {
          product.setStoreName(store.getName());
          return categoryGateway.findById(product.getCategoryId())
              .switchIfEmpty(Mono.error(new IllegalArgumentException("La categoría asociada no existe")));
        })
        .flatMap(category -> {
          product.setCategoryName(category.getName());
          return productGateway.save(product);
        });
  }

  public Mono<Product> updateProduct(String id, Product product) {
    validateNotEmpty(id, "El ID del producto es requerido para actualizar");
    validateNotNull(product, "El producto no puede ser nulo");
    validateNotEmpty(product.getName(), "El nombre del producto es requerido");
    validateNotNull(product.getPrice(), "El precio es requerido");
    validate(product.getPrice() > 0, "El precio del producto debe ser mayor a 0");
    validateNotEmpty(product.getStoreId(), "El ID de la tienda es requerido");
    validateNotEmpty(product.getCategoryId(), "El ID de la categoría es requerido");

    return productGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto con ID " + id + " no existe")))
        .flatMap(existingProduct -> {
          product.setId(existingProduct.getId());
          product.setCreatedDate(existingProduct.getCreatedDate());
          product.setUpdatedDate(LocalDateTime.now());

          return storeGateway.findById(product.getStoreId())
              .switchIfEmpty(Mono.error(new IllegalArgumentException("La tienda asociada no existe")))
              .flatMap(store -> {
                product.setStoreName(store.getName());
                return categoryGateway.findById(product.getCategoryId())
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("La categoría asociada no existe")));
              });
        })
        .flatMap(category -> {
          product.setCategoryName(category.getName());
          return productGateway.update(product);
        });
  }

  public Mono<Product> getProductById(String id) {
    validateNotEmpty(id, "El ID es requerido para buscar");
    return productGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Producto no encontrado")));
  }

  public Flux<Product> getAllProducts() {
    return productGateway.findAll();
  }

  public Mono<Void> deleteProduct(String id) {
    validateNotEmpty(id, "El ID es requerido para eliminar");
    return productGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("No se puede eliminar: El producto no existe")))
        .flatMap(product -> productGateway.deleteById(id));
  }

  public Flux<Product> searchProductsByName(String name) {
    validateNotEmpty(name, "El parámetro de búsqueda no puede estar vacío");
    return productGateway.findByNameContainingIgnoreCase(name);
  }

  public Flux<Product> filterProductsByStore(String storeId) {
    validateNotEmpty(storeId, "El ID de la tienda no puede estar vacío");
    return productGateway.findByStoreId(storeId);
  }

  public Flux<Product> filterProductsByCategory(String categoryId) {
    validateNotEmpty(categoryId, "El ID de la categoría no puede estar vacío");
    return productGateway.findByCategoryId(categoryId);
  }
}