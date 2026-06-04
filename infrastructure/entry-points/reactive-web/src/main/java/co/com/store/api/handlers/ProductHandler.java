package co.com.store.api.handlers;

import co.com.store.api.BaseHandler;
import co.com.store.model.Product;
import co.com.store.usecase.ProductUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Optional;

@Component
public class ProductHandler extends BaseHandler {

    private final ProductUseCase productUseCase;

    public ProductHandler(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Optional<String> nameParam = request.queryParam("name");
        Optional<String> storeParam = request.queryParam("storeId");
        Optional<String> categoryParam = request.queryParam("categoryId");

        if (nameParam.isPresent()) {
            return productUseCase.searchProductsByName(nameParam.get())
                    .collectList().flatMap(this::handleSuccess)
                    .onErrorResume(this::handleInternalError);
        } else if (storeParam.isPresent()) {
            return productUseCase.filterProductsByStore(storeParam.get())
                    .collectList().flatMap(this::handleSuccess)
                    .onErrorResume(this::handleInternalError);
        } else if (categoryParam.isPresent()) {
            return productUseCase.filterProductsByCategory(categoryParam.get())
                    .collectList().flatMap(this::handleSuccess)
                    .onErrorResume(this::handleInternalError);
        }

        return productUseCase.getAllProducts()
                .collectList()
                .flatMap(this::handleSuccess)
                .onErrorResume(this::handleInternalError);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        return productUseCase.getProductById(id)
                .flatMap(this::handleSuccess)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Product.class)
                .flatMap(productUseCase::createProduct)
                .flatMap(this::handleCreated)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(Product.class)
                .flatMap(product -> productUseCase.updateProduct(id, product))
                .flatMap(this::handleSuccess)
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return productUseCase.deleteProduct(id)
                .then(handleNoContent())
                .onErrorResume(e -> handleBadRequest(e.getMessage()));
    }
}