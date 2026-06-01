package co.com.store.mongo.Collections;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

/**
 * EJEMPLO: Documentos MongoDB para Historial
 * 
 * Reemplaza esta clase con tus propios documentos.
 * Para crear un nuevo documento:
 * 1. Crea una clase con @Document(collection = "nombre-coleccion")
 * 2. Define los campos con @Id para el identificador único
 * 3. Usa Lombok para getters/setters (@Getter, @Setter)
 */
@Getter
@Setter
@Document(collection = "historial")
public class HistorialCollections {
    private String id;
    private Integer contador;
    private String descripcion;
}
