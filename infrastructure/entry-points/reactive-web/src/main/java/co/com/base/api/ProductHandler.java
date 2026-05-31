package co.com.base.api;

import co.com.base.usecase.CreateProductUseCase;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Handler de ejemplo: Maneja solicitudes HTTP relacionadas con productos.
 * 
 * Demuestra el patrón:
 *   HTTP Request → ProductHandler → CreateProductUseCase → Gateway (MongoDB)
 * 
 * El handler extrae datos del request, delega en el caso de uso,
 * y formatea la respuesta HTTP.
 * 
 * NOTA: Este es un ejemplo. Para usarlo en tu aplicación:
 * 1. Marca la clase como @Component o @Service
 * 2. Inyéctalo en ApiRouterConfig
 * 3. Agrega las rutas en el método apiRoutes()
 */
// @Component  // <-- Comenta esta línea cuando integres el handler
public class ProductHandler extends BaseHandler {

    private final CreateProductUseCase createProductUseCase;

    public ProductHandler(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }

    /**
     * Maneja POST /products: crear un nuevo producto.
     * 
     * @param request La solicitud HTTP (contiene el body con datos del producto)
     * @return Mono<ServerResponse> con la respuesta formateada
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .flatMap(body -> (Mono<Map<String, String>>) (Mono<?>) this.createProductUseCase.execute((Map<String, String>) body))
                .flatMap(this::handleCreated)
                .onErrorResume(error -> handleErrorResponse((Throwable) error));
    }

    /**
     * Maneja GET /products/{id}: obtener un producto por ID.
     * TODO: Implementar con GetProductByIdUseCase
     * 
     * @param request La solicitud HTTP (parámetro: id)
     * @return Mono<ServerResponse> con el producto encontrado o un 404
     */
    public Mono<ServerResponse> getById(ServerRequest request) {
        // Obtener el ID pero implementación futura...
        request.pathVariable("id");
        return Mono.error(new UnsupportedOperationException("Implementar GetProductByIdUseCase"));
    }

    /**
     * Maneja errores según su tipo y retorna la respuesta HTTP apropiada.
     * 
     * @param throwable La excepción ocurrida
     * @return Mono<ServerResponse> con el error HTTP formateado
     */
    private Mono<ServerResponse> handleErrorResponse(Throwable throwable) {
        if (throwable instanceof IllegalArgumentException) {
            return this.handleBadRequest(throwable.getMessage());
        }
        return this.handleInternalError(throwable);
    }
}
