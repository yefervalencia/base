package co.com.store.model.gateways;

import co.com.store.model.Category;
import reactor.core.publisher.Flux;

public interface CategoryGateway extends BaseGateway<Category, String> {
    Flux<Category> findAll();
    Flux<Category> findByNameContainingIgnoreCase(String name);
}