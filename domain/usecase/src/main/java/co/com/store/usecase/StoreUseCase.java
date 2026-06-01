package co.com.store.usecase;

import org.springframework.stereotype.Service;
import co.com.store.model.Store;
import co.com.store.model.gateways.StoreGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class StoreUseCase extends BaseUseCase {

  private final StoreGateway storeGateway;

  public StoreUseCase(StoreGateway storeGateway) {
    super();
    this.storeGateway = storeGateway;
  }

  public Mono<Store> createStore(Store store) {
    validateNotNull(store, "La tienda no puede ser nula");
    validateNotEmpty(store.getName(), "El nombre de la tienda es requerido");
    validateNotEmpty(store.getAddress(), "La dirección de la tienda es requerida");

    LocalDateTime ahora = LocalDateTime.now();
    store.setCreatedDate(ahora);
    store.setUpdatedDate(ahora);

    return storeGateway.save(store);
  }

  public Mono<Store> updateStore(String id, Store store) {
    validateNotEmpty(id, "El ID de la tienda es requerido para actualizar");
    validateNotNull(store, "La tienda no puede ser nula");
    validateNotEmpty(store.getName(), "El nombre de la tienda es requerido");

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
    validateNotEmpty(id, "El ID de búsqueda es requerido");
    return storeGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Tienda no encontrada")));
  }

  public Flux<Store> getAllStores() {
    return storeGateway.findAll();
  }

  public Mono<Void> deleteStore(String id) {
    validateNotEmpty(id, "El ID es requerido para eliminar");
    return storeGateway.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("No se puede eliminar: Tienda no existe")))
        .flatMap(store -> storeGateway.deleteById(id));
  }
}