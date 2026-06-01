package co.com.store.mongo.Collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stores")
public class StoreCollections {
    @Id
    private String id;
    private String name;
    private String address;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}