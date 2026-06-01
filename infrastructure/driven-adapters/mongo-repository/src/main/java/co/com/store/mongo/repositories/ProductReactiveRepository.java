package co.com.store.mongo.repositories;

import co.com.store.mongo.collections.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductReactiveRepository extends ReactiveMongoRepository<ProductDocument, String> {
  Flux<ProductDocument> findByNameContainingIgnoreCase(String name);

  Flux<ProductDocument> findByStoreId(String storeId);

  Flux<ProductDocument> findByCategoryId(String categoryId);

  Mono<Void> deleteByStoreId(String storeId);
  Mono<Void> deleteByCategoryId(String categoryId);
}
