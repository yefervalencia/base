// Crea ambos archivos en la misma carpeta con la misma estructura:
package co.com.store.mongo.repositories;

import co.com.store.mongo.collections.StoreDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StoreReactiveRepository extends ReactiveMongoRepository<StoreDocument, String> {
  Flux<StoreDocument> findByNameContainingIgnoreCase(String name);
}