package co.com.store.api.handlers;

import co.com.store.api.BaseHandler;
import co.com.store.model.Category;
import co.com.store.usecase.CategoryUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CategoryHandler extends BaseHandler {

    private final CategoryUseCase categoryUseCase;

    public CategoryHandler(CategoryUseCase categoryUseCase) {
        this.categoryUseCase = categoryUseCase;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return categoryUseCase.getAllCategories()
                .collectList()
                .flatMap(this::handleSuccess) // <-- Usamos handleSuccess
                .onErrorResume(this::handleInternalError);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        return categoryUseCase.getCategoryById(id)
                .flatMap(this::handleSuccess)
                .onErrorResume(e -> handleBadRequest(e.getMessage())); // <-- Pasamos el String del error
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Category.class)
                .flatMap(categoryUseCase::createCategory)
                .flatMap(this::handleCreated)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(Category.class)
                .flatMap(category -> categoryUseCase.updateCategory(id, category))
                .flatMap(this::handleSuccess)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return categoryUseCase.deleteCategory(id)
                .then(handleNoContent()) // <-- No necesita this:: porque no recibe parámetros
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }
}