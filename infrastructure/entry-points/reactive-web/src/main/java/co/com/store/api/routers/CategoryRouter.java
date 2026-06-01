package co.com.store.api.routers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.store.api.handlers.*;

@Component
public class CategoryRouter {

    private final CategoryHandler handler;

    public CategoryRouter(CategoryHandler handler) {
        this.handler = handler;
    }

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/categories", handler::getAll)
                .GET("/categories/{id}", handler::getById)
                .POST("/categories", handler::create)
                .PUT("/categories/{id}", handler::update)
                .DELETE("/categories/{id}", handler::delete)
                .build();
    }
}