package co.com.store.mongo.implementations;

import co.com.store.model.Product;
import co.com.store.model.gateways.ProductGateway;
import co.com.store.mongo.mappers.ProductMapper;
import co.com.store.mongo.repositories.ProductReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductGatewayImpl implements ProductGateway {

    private final ProductReactiveRepository repository;
    private final ProductMapper mapper;

    public ProductGatewayImpl(ProductReactiveRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product entity) {
        return repository.save(mapper.toDocument(entity)).map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Product> update(Product entity) {
        return save(entity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Product> findAll() {
        return repository.findAll().map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByNameContainingIgnoreCase(String name) {
        return repository.findByNameContainingIgnoreCase(name).map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByStoreId(String storeId) {
        return repository.findByStoreId(storeId).map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByCategoryId(String categoryId) {
        return repository.findByCategoryId(categoryId).map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteByStoreId(String storeId) {
        return repository.deleteByStoreId(storeId);
    }

    @Override
    public Mono<Void> deleteByCategoryId(String categoryId) {
        return repository.deleteByCategoryId(categoryId);
    }
}