package co.com.store.model.gateways;

import reactor.core.publisher.Mono;

/**
 * Interfaz base genérica para todos los gateways del proyecto.
 * Define el contrato entre el dominio y la infraestructura.
 * 
 * @param <T>  Tipo de entidad que maneja el gateway
 * @param <ID> Tipo del identificador único
 */
public interface BaseGateway<T, ID> {

    /**
     * Guarda una entidad
     * 
     * @param entity Entidad a guardar
     * @return Mono con la entidad guardada
     */
    Mono<T> save(T entity);

    /**
     * Busca una entidad por su ID
     * 
     * @param id Identificador único
     * @return Mono con la entidad encontrada o vacío
     */
    Mono<T> findById(ID id);

    /**
     * Actualiza una entidad existente
     * 
     * @param entity Entidad a actualizar
     * @return Mono con la entidad actualizada
     */
    Mono<T> update(T entity);

    /**
     * Elimina una entidad por su ID
     * 
     * @param id Identificador único
     * @return Mono vacío cuando se completa
     */
    Mono<Void> deleteById(ID id);
}
