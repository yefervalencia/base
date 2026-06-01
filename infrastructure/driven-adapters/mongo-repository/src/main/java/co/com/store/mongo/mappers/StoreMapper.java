package co.com.store.mongo.mappers;

import co.com.store.model.Store;
import co.com.store.mongo.collections.StoreDocument;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    
    public Store toDomain(StoreDocument document) {
        if (document == null) return null;
        return Store.builder()
                .id(document.getId())
                .name(document.getName())
                .address(document.getAddress())
                .createdDate(document.getCreatedDate())
                .updatedDate(document.getUpdatedDate())
                .build();
    }
    
    public StoreDocument toDocument(Store domain) {
        if (domain == null) return null;
        return StoreDocument.builder()
                .id(domain.getId())
                .name(domain.getName())
                .address(domain.getAddress())
                .createdDate(domain.getCreatedDate())
                .updatedDate(domain.getUpdatedDate())
                .build();
    }
}