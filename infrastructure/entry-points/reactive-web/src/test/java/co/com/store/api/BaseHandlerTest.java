package co.com.store.api;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

class BaseHandlerTest {

        private final BaseHandler handler = new BaseHandler() {
        };

        @Test
        void handleSuccessShouldReturn200WithBody() {
                WebTestClient client = WebTestClient.bindToRouterFunction(
                                route(GET("/success"), request -> handler.handleSuccess("ok"))).build();

                client.get()
                                .uri("/success")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(String.class).isEqualTo("ok");
        }

        @Test
        void handleCreatedShouldReturn201WithBody() {
                WebTestClient client = WebTestClient.bindToRouterFunction(
                                route(GET("/created"), request -> handler.handleCreated("created"))).build();

                client.get()
                                .uri("/created")
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(String.class).isEqualTo("created");
        }

        @Test
        void handleNoContentShouldReturn204() {
                WebTestClient client = WebTestClient.bindToRouterFunction(
                                route(GET("/nocontent"), request -> handler.handleNoContent())).build();

                client.get()
                                .uri("/nocontent")
                                .exchange()
                                .expectStatus().isNoContent();
        }

        @Test
        void handleBadRequestShouldReturn400WithErrorBody() {
                WebTestClient client = WebTestClient.bindToRouterFunction(
                                route(GET("/bad"), request -> handler.handleBadRequest("invalid"))).build();

                client.get()
                                .uri("/bad")
                                .exchange()
                                .expectStatus().isBadRequest()
                                .expectBody()
                                .jsonPath("$.status").isEqualTo(400)
                                .jsonPath("$.message").isEqualTo("invalid");
        }
}
