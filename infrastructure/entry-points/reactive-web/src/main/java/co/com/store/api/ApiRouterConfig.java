package co.com.store.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.store.api.handlers.CategoryHandler;
import co.com.store.api.handlers.ProductHandler;
import co.com.store.api.handlers.StoreHandler;
import co.com.store.api.routers.CategoryRouter;
import co.com.store.api.routers.ProductRouter;
import co.com.store.api.routers.StoreRouter;

import java.util.Map;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * Configuración de rutas HTTP usando RouterFunction (enfoque funcional de
 * Spring WebFlux).
 * 
 * Cada ruta delega en un Handler, que a su vez invoca el UseCase.
 * Esta arquitectura preserva la separación de capas:
 * HTTP Layer (RouterFunction) → API Layer (Handler) → Business Logic (UseCase)
 * → Data (Gateway)
 * 
 * NOTA: Para agregar rutas de negocio, inyecta el handler correspondiente
 * (ej: ProductHandler) y agrega rutas en el método apiRoutes().
 * Los handlers deben ser @Component o @Service.
 */
@Configuration
public class ApiRouterConfig {

    // TODO: Inyectar handlers de negocio aquí (ej: private final ProductHandler
    // productHandler;)
    // private final ProductHandler productHandler;

    // public ApiRouterConfig(ProductHandler productHandler) {
    // this.productHandler = productHandler;
    // }

    private final CategoryRouter categoryRouter;
    private final StoreRouter storeRouter;
    private final ProductRouter productRouter;

    public ApiRouterConfig(CategoryRouter categoryRouter, StoreRouter storeRouter, ProductRouter productRouter) {
        this.categoryRouter = categoryRouter;
        this.storeRouter = storeRouter;
        this.productRouter = productRouter;
    }

    @Bean
    public RouterFunction<ServerResponse> apiRoutes() {
        return RouterFunctions.route(GET("/health"), request -> ServerResponse.ok()
                .bodyValue(Objects.requireNonNull(Map.of("status", "UP"))))
                .and(RouterFunctions.nest(path("/apistore/v1"),
                        categoryRouter.routes()
                        .and(storeRouter.routes())
                        .and(productRouter.routes())
                ));
    }
}
