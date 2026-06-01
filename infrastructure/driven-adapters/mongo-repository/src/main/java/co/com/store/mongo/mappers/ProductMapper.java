package co.com.store.mongo.mappers;

import co.com.store.model.Product;
import co.com.store.mongo.collections.ProductDocument;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public Product toDomain(ProductDocument document) {
        if (document == null) return null;
        return Product.builder()
                .id(document.getId())
                .name(document.getName())
                .price(document.getPrice())
                .storeId(document.getStoreId())
                .storeName(document.getStoreName())
                .categoryId(document.getCategoryId())
                .categoryName(document.getCategoryName())
                .createdDate(document.getCreatedDate())
                .updatedDate(document.getUpdatedDate())
                .build();
    }
    
    public ProductDocument toDocument(Product domain) {
        if (domain == null) return null;
        return ProductDocument.builder()
                .id(domain.getId())
                .name(domain.getName())
                .price(domain.getPrice())
                .storeId(domain.getStoreId())
                .storeName(domain.getStoreName())
                .categoryId(domain.getCategoryId())
                .categoryName(domain.getCategoryName())
                .createdDate(domain.getCreatedDate())
                .updatedDate(domain.getUpdatedDate())
                .build();
    }
}