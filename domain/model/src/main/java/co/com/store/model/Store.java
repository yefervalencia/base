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
public class Store {
    private String id;
    private String name;
    private String address;
    private LocalDateTime cretedDate;
    private LocalDateTime updatedDate;
}