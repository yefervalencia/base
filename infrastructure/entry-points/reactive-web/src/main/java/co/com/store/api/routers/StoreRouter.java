package co.com.store.api.routers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.store.api.handlers.*;

@Component
public class StoreRouter {

    private final StoreHandler handler;

    public StoreRouter(StoreHandler handler) {
        this.handler = handler;
    }

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/stores", handler::getAll)
                .GET("/stores/{id}", handler::getById)
                .POST("/stores", handler::create)
                .PUT("/stores/{id}", handler::update)
                .DELETE("/stores/{id}", handler::delete)
                .build();
    }
}