package co.com.store.mongo.collections;

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
@Document(collection = "products")
public class ProductDocument {
    @Id
    private String id;
    private String name;
    private Double price;
    private String storeId;
    private String storeName;
    private String categoryId;
    private String categoryName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}