package co.com.store.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Clase base para todos los handlers de API del proyecto.
 * Proporciona métodos comunes para respuestas HTTP estandarizadas.
 */
public abstract class BaseHandler {

    /**
     * Crea una respuesta de éxito con datos
     * 
     * @param data Datos a retornar
     * @return Mono con respuesta 200 OK
     */
    protected Mono<ServerResponse> handleSuccess(Object data) {
        return ServerResponse
                .ok()
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .bodyValue(Objects.requireNonNull(data));
    }

    /**
     * Crea una respuesta 201 Created
     * 
     * @param data Datos creados
     * @return Mono con respuesta 201 Created
     */
    protected Mono<ServerResponse> handleCreated(Object data) {
        return ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .bodyValue(Objects.requireNonNull(data));
    }

    /**
     * Crea una respuesta 204 No Content
     * 
     * @return Mono con respuesta vacía
     */
    protected Mono<ServerResponse> handleNoContent() {
        return ServerResponse.noContent().build();
    }

    /**
     * Maneja errores de validación (400 Bad Request)
     * 
     * @param message Mensaje de error
     * @return Mono con respuesta de error
     */
    protected Mono<ServerResponse> handleBadRequest(String message) {
        return handleError(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Maneja errores de no encontrado (404 Not Found)
     * 
     * @param message Mensaje de error
     * @return Mono con respuesta de error
     */
    protected Mono<ServerResponse> handleNotFound(String message) {
        return handleError(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Maneja errores internos (500 Internal Server Error)
     * 
     * @param error Excepción ocurrida
     * @return Mono con respuesta de error
     */
    protected Mono<ServerResponse> handleInternalError(Throwable error) {
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR,
                error.getMessage() != null ? error.getMessage() : "Error interno del servidor");
    }

    /**
     * Método genérico para manejar errores
     * 
     * @param status  Estado HTTP
     * @param message Mensaje de error
     * @return Mono con respuesta de error
     */
    private Mono<ServerResponse> handleError(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return ServerResponse
                .status(status)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .bodyValue(errorResponse);
    }

    /**
     * Clase interna para respuestas de error estandarizadas
     */
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
