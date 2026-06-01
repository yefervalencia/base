package co.com.store.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
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