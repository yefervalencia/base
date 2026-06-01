package co.com.store.mongo.implementations;

import co.com.store.model.Category;
import co.com.store.model.gateways.CategoryGateway;
import co.com.store.mongo.mappers.CategoryMapper;
import co.com.store.mongo.repositories.CategoryReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryGatewayImpl implements CategoryGateway {

    private final CategoryReactiveRepository repository;
    private final CategoryMapper mapper;

    public CategoryGatewayImpl(CategoryReactiveRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Category> save(Category entity) {
        return repository.save(mapper.toDocument(entity)).map(mapper::toDomain);
    }

    @Override
    public Mono<Category> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Category> update(Category entity) {
        return save(entity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Category> findAll() {
        return repository.findAll().map(mapper::toDomain);
    }

    @Override
    public Flux<Category> findByNameContainingIgnoreCase(String name) {
        return repository.findByNameContainingIgnoreCase(name).map(mapper::toDomain);
    }
}