package co.com.store.usecase;

import co.com.store.model.Product;
import co.com.store.model.gateways.ProductGateway;
import co.com.store.model.gateways.StoreGateway;
import co.com.store.model.gateways.CategoryGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public class ProductUseCase extends BaseUseCase {

  private final ProductGateway productGateway;
  private final StoreGateway storeGateway;
  private final CategoryGateway categoryGateway;

  public ProductUseCase(ProductGateway productGateway, StoreGateway storeGateway, CategoryGateway categoryGateway) {
    this.productGateway = productGateway;
    this.storeGateway = storeGateway;
    this.categoryGateway = categoryGateway;
  }

  public Mono<Product> createProduct(Product product) {
    if (product.getPrice() == null || product.getPrice() < 0) {
      return Mono.error(new IllegalArgumentException("El precio del producto no puede ser negativo o nulo"));
    }

    product.setCreatedDate(LocalDateTime.now());
    product.setUpdatedDate(LocalDateTime.now());

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
    if (product.getPrice() == null || product.getPrice() < 0) {
      return Mono.error(new IllegalArgumentException("El precio del producto no puede ser negativo"));
    }

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
    return productGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Producto no encontrado")));
  }

  public Flux<Product> getAllProducts() {
    return productGateway.findAll();
  }

  public Mono<Void> deleteProduct(String id) {
    return productGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("No se puede eliminar: El producto no existe")))
        .flatMap(product -> productGateway.deleteById(id));
  }

  public Flux<Product> searchProductsByName(String name) {
    return productGateway.findByNameContainingIgnoreCase(name);
  }

  public Flux<Product> filterProductsByStore(String storeId) {
    return productGateway.findByStoreId(storeId);
  }

  public Flux<Product> filterProductsByCategory(String categoryId) {
    return productGateway.findByCategoryId(categoryId);
  }
}