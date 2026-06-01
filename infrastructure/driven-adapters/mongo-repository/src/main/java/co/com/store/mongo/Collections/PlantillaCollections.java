package co.com.store.mongo.Collections;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

/**
 * EJEMPLO: Documentos MongoDB para Plantilla
 * 
 * Reemplaza esta clase con tus propios documentos.
 * Para crear un nuevo documento:
 * 1. Crea una clase con @Document(collection = "nombre-coleccion")
 * 2. Define los campos con @Id para el identificador único
 * 3. Usa Lombok para getters/setters (@Data incluye getters, setters, equals,
 * hashCode, toString)
 */
@Data
@Document(collection = "plantilla")
public class PlantillaCollections {

    @Id
    private String id;
    private String nombre;
    private Date fecha;
    private String descripcion;
    private Date fechaActualizacion;
}
