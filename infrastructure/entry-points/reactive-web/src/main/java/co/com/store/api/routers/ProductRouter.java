package co.com.store.api.routers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.store.api.handlers.*;

@Component
public class ProductRouter {

    private final ProductHandler handler;

    public ProductRouter(ProductHandler handler) {
        this.handler = handler;
    }

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/products", handler::getAll)
                .GET("/products/{id}", handler::getById)
                .POST("/products", handler::create)
                .PUT("/products/{id}", handler::update)
                .DELETE("/products/{id}", handler::delete)
                .build();
    }
}