package co.com.base.mongo;

import co.com.base.model.gateways.BaseGateway;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * EJEMPLO: Implementación base de un Gateway con MongoDB Reactivo
 * 
 * Para implementar tu propio gateway:
 * 1. Crea una interfaz que extienda ReactiveMongoRepository
 * 2. Crea una clase @Repository que implemente tu gateway
 * 3. Inyecta el repositorio y mapea datos entre entidad del modelo y documento MongoDB
 * 
 * @param <T> Entidad del modelo
 * @param <D> Documento MongoDB
 * @param <R> Repository reactivo
 */
public abstract class BaseMongoGatewayImpl<T, D, R extends ReactiveMongoRepository<D, String>> implements BaseGateway<T, String> {
    
    protected final R repository;
    
    public BaseMongoGatewayImpl(R repository) {
        this.repository = repository;
    }
    
    /**
     * Convierte un documento MongoDB a entidad del modelo
     */
    protected abstract T toDomain(D document);
    
    /**
     * Convierte una entidad del modelo a documento MongoDB
     */
    protected abstract D toDocument(T domain);
    
    @Override
    public Mono<T> save(T entity) {
        D document = Objects.requireNonNull(toDocument(entity));
        return repository.save(document)
                .map(this::toDomain);
    }
    
    @Override
    public Mono<T> findById(String id) {
        return repository.findById(Objects.requireNonNull(id))
                .map(this::toDomain);
    }
    
    @Override
    public Mono<T> update(T entity) {
        return save(entity);
    }
    
    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(Objects.requireNonNull(id));
    }
}
