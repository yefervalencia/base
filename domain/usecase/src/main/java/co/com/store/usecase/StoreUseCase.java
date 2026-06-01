package co.com.store.usecase;

import co.com.store.model.Store;
import co.com.store.model.gateways.StoreGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public class StoreUseCase extends BaseUseCase {

  private final StoreGateway storeGateway;

  public StoreUseCase(StoreGateway storeGateway) {
    this.storeGateway = storeGateway;
  }

  public Mono<Store> createStore(Store store) {
    store.setCreatedDate(LocalDateTime.now());
    store.setUpdatedDate(LocalDateTime.now());
    return storeGateway.save(store);
  }

  public Mono<Store> updateStore(String id, Store store) {
    return storeGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("La tienda con ID " + id + " no existe")))
        .flatMap(existingStore -> {
          store.setId(existingStore.getId());
          store.setCreatedDate(existingStore.getCreatedDate());
          store.setUpdatedDate(LocalDateTime.now());
          return storeGateway.update(store);
        });
  }

  public Mono<Store> getStoreById(String id) {
    return storeGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Tienda no encontrada")));
  }

  public Flux<Store> getAllStores() {
    return storeGateway.findAll();
  }

  public Mono<Void> deleteStore(String id) {
    return storeGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("No se puede eliminar: Tienda no existe")))
        .flatMap(store -> storeGateway.deleteById(id));
  }
}