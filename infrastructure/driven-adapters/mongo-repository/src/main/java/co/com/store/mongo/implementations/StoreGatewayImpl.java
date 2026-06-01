package co.com.store.mongo.implementations;

import co.com.store.model.Store;
import co.com.store.model.gateways.StoreGateway;
import co.com.store.mongo.mappers.StoreMapper;
import co.com.store.mongo.repositories.StoreReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StoreGatewayImpl implements StoreGateway {

    private final StoreReactiveRepository repository;
    private final StoreMapper mapper;

    public StoreGatewayImpl(StoreReactiveRepository repository, StoreMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Store> save(Store entity) {
        return repository.save(mapper.toDocument(entity)).map(mapper::toDomain);
    }

    @Override
    public Mono<Store> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Store> update(Store entity) {
        return save(entity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Store> findAll() {
        return repository.findAll().map(mapper::toDomain);
    }

    @Override
    public Flux<Store> findByNameContainingIgnoreCase(String name) {
        return repository.findByNameContainingIgnoreCase(name).map(mapper::toDomain);
    }
}