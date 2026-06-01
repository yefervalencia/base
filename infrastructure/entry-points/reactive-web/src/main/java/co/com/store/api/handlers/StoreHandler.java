package co.com.store.api.handlers;

import co.com.store.api.BaseHandler;
import co.com.store.model.Store;
import co.com.store.usecase.StoreUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StoreHandler extends BaseHandler {

    private final StoreUseCase storeUseCase;

    public StoreHandler(StoreUseCase storeUseCase) {
        this.storeUseCase = storeUseCase;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return storeUseCase.getAllStores()
                .collectList()
                .flatMap(this::handleSuccess)
                .onErrorResume(this::handleInternalError);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        return storeUseCase.getStoreById(id)
                .flatMap(this::handleSuccess)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Store.class)
                .flatMap(storeUseCase::createStore)
                .flatMap(this::handleCreated)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(Store.class)
                .flatMap(store -> storeUseCase.updateStore(id, store))
                .flatMap(this::handleSuccess)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return storeUseCase.deleteStore(id)
                .then(handleNoContent())
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }
}