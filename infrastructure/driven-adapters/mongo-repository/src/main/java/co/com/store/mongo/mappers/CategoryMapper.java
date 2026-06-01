package co.com.store.mongo.mappers;

import co.com.store.model.Category;
import co.com.store.mongo.collections.CategoryDocument;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    
    public Category toDomain(CategoryDocument document) {
        if (document == null) return null;
        return Category.builder()
                .id(document.getId())
                .name(document.getName())
                .description(document.getDescription())
                .createdDate(document.getCreatedDate())
                .updatedDate(document.getUpdatedDate())
                .build();
    }
    
    public CategoryDocument toDocument(Category domain) {
        if (domain == null) return null;
        return CategoryDocument.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .createdDate(domain.getCreatedDate())
                .updatedDate(domain.getUpdatedDate())
                .build();
    }
}