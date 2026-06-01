package co.com.store.model.gateways;

import co.com.store.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductGateway extends BaseGateway<Product, String> {
    Flux<Product> findAll();
    Flux<Product> findByNameContainingIgnoreCase(String name);
    Flux<Product> findByStoreId(String storeId);
    
    Flux<Product> findByCategoryId(String categoryId);

    Mono<Void> deleteByStoreId(String storeId);
    Mono<Void> deleteByCategoryId(String categoryId);
}