package co.com.base.usecase;

import co.com.base.model.gateways.BaseGateway;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Use case de ejemplo: Crear un producto.
 * Demuestra cómo un caso de uso valida entrada, delega a un gateway (repositorio)
 * y retorna el resultado de forma reactiva.
 */
public class CreateProductUseCase extends BaseUseCase {

    private final BaseGateway<Map<String, String>, String> gateway;

    public CreateProductUseCase(BaseGateway<Map<String, String>, String> gateway) {
        this.validateNotNull(gateway, "Gateway no puede ser nulo");
        this.gateway = gateway;
    }

    /**
     * Ejecuta la lógica de creación de un producto.
     * 
     * @param productData Mapa con los datos del producto (nombre, descripción, etc.)
     * @return Mono con el producto creado (incluye ID generado por la base de datos)
     */
    public Mono<Map<String, String>> execute(Map<String, String> productData) {
        this.validateNotNull(productData, "Datos del producto no pueden ser nulos");
        
        return this.gateway.save(productData);
    }
}
