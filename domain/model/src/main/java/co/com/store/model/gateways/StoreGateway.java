package co.com.store.model.gateways;

import co.com.store.model.Store;
import reactor.core.publisher.Flux;

public interface StoreGateway extends BaseGateway<Store, String> {
    
    Flux<Store> findAll();
    Flux<Store> findByNameContainingIgnoreCase(String name);
}