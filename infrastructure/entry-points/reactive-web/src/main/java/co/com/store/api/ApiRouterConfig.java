package co.com.store.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

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

    @Bean
    public RouterFunction<ServerResponse> apiRoutes() {
        return RouterFunctions.route(GET("/health"), request -> ServerResponse.ok()
                .bodyValue(Objects.requireNonNull(Map.of("status", "UP"))));
        // Ejemplo de cómo agregar rutas de negocio:
        // return RouterFunctions.route(GET("/health"), ...)
        // .andRoute(POST("/products"), productHandler::create)
        // .andRoute(GET("/products/{id}"), productHandler::getById);
    }
}
