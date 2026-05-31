# Ejemplos Prácticos de Implementación

Este archivo contiene ejemplos paso a paso de cómo implementar características usando las clases base del proyecto.

## 1. Crear una Entidad (Modelo)

Crea un archivo en `domain/model/src/main/java/co/com/base/model/`:

```java
package co.com.base.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {
    private String id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
```

## 2. Crear un Gateway (Interfaz de Repositorio)

Crea un archivo en `domain/model/src/main/java/co/com/base/model/gateways/`:

```java
package co.com.base.model.gateways;

import co.com.base.model.Product;

/**
 * Gateway para Product.
 * Extiende BaseGateway para heredar operaciones CRUD comunes.
 */
public interface ProductGateway extends BaseGateway<Product, String> {
    // Aquí puedes agregar métodos adicionales específicos
    // Ej: Mono<List<Product>> findByPrecioGreaterThan(Double precio);
}
```

## 3. Crear un Documento MongoDB

Crea un archivo en `infrastructure/driven-adapters/mongo-repository/src/main/java/co/com/base/mongo/Collections/`:

```java
package co.com.base.mongo.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "products")
public class ProductDocument {
    @Id
    private String id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
```

## 4. Crear un Mapper

Crea un archivo en `infrastructure/driven-adapters/mongo-repository/src/main/java/co/com/base/mongo/Mappers/`:

```java
package co.com.base.mongo.Mappers;

import co.com.base.model.Product;
import co.com.base.mongo.Collections.ProductDocument;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Product (modelo) y ProductDocument (MongoDB)
 */
@Component
public class ProductMapper {
    
    public Product toDomain(ProductDocument document) {
        if (document == null) return null;
        
        Product product = new Product();
        product.setId(document.getId());
        product.setNombre(document.getNombre());
        product.setPrecio(document.getPrecio());
        product.setDescripcion(document.getDescripcion());
        product.setFechaCreacion(document.getFechaCreacion());
        product.setFechaActualizacion(document.getFechaActualizacion());
        return product;
    }
    
    public ProductDocument toDocument(Product product) {
        if (product == null) return null;
        
        ProductDocument document = new ProductDocument();
        document.setId(product.getId());
        document.setNombre(product.getNombre());
        document.setPrecio(product.getPrecio());
        document.setDescripcion(product.getDescripcion());
        document.setFechaCreacion(product.getFechaCreacion());
        document.setFechaActualizacion(product.getFechaActualizacion());
        return document;
    }
}
```

## 5. Crear un Repository Reactivo

Crea un archivo en `infrastructure/driven-adapters/mongo-repository/src/main/java/co/com/base/mongo/Repositories/`:

```java
package co.com.base.mongo.Repositories;

import co.com.base.mongo.Collections.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository reactivo para ProductDocument.
 * Spring Data MongoDB proporciona operaciones CRUD automáticas.
 */
@Repository
public interface ProductReactiveRepository extends ReactiveMongoRepository<ProductDocument, String> {
    // Aquí puedes definir métodos de consulta personalizados
    // Ej: Flux<ProductDocument> findByNombreContaining(String nombre);
}
```

## 6. Implementar el Gateway

Crea un archivo en `infrastructure/driven-adapters/mongo-repository/src/main/java/co/com/base/mongo/Implementations/`:

```java
package co.com.base.mongo.Implementations;

import co.com.base.model.Product;
import co.com.base.model.gateways.ProductGateway;
import co.com.base.mongo.Collections.ProductDocument;
import co.com.base.mongo.Mappers.ProductMapper;
import co.com.base.mongo.Repositories.ProductReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementación de ProductGateway usando MongoDB Reactivo
 */
@Service
public class ProductGatewayImpl implements ProductGateway {
    
    private final ProductReactiveRepository repository;
    private final ProductMapper mapper;
    
    public ProductGatewayImpl(ProductReactiveRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public Mono<Product> save(Product entity) {
        ProductDocument document = mapper.toDocument(entity);
        return repository.save(document)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Product> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Product> update(Product entity) {
        return save(entity);
    }
    
    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
```

## 7. Crear un Caso de Uso

Crea un archivo en `domain/usecase/src/main/java/co/com/base/usecase/product/`:

```java
package co.com.base.usecase.product;

import co.com.base.model.Product;
import co.com.base.model.gateways.ProductGateway;
import co.com.base.usecase.BaseUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

/**
 * Caso de uso para crear un producto
 */
@Service
public class CreateProductUseCase extends BaseUseCase {
    
    private final ProductGateway gateway;
    
    public CreateProductUseCase(ProductGateway gateway) {
        super();
        this.gateway = gateway;
    }
    
    public Mono<Product> execute(Product product) {
        // Validaciones
        validateNotNull(product, "El producto no puede ser nulo");
        validateNotEmpty(product.getNombre(), "El nombre del producto es requerido");
        validate(product.getPrecio() > 0, "El precio debe ser mayor a 0");
        
        // Establecer valores por defecto
        LocalDateTime ahora = LocalDateTime.now();
        if (product.getFechaCreacion() == null) {
            product.setFechaCreacion(ahora);
        }
        product.setFechaActualizacion(ahora);
        
        // Guardar usando el gateway
        return gateway.save(product);
    }
}
```

## 8. Crear un Handler (Endpoint)

Crea un archivo en `infrastructure/entry-points/reactive-web/src/main/java/co/com/base/api/product/`:

```java
package co.com.base.api.product;

import co.com.base.api.BaseHandler;
import co.com.base.model.Product;
import co.com.base.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Handler para operaciones de Producto
 */
@Component
public class ProductHandler extends BaseHandler {
    
    private final CreateProductUseCase createProductUseCase;
    
    public ProductHandler(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }
    
    /**
     * Endpoint POST para crear un producto
     */
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Product.class)
                .flatMap(product -> createProductUseCase.execute(product))
                .flatMap(this::handleCreated)
                .onErrorResume(error -> {
                    if (error instanceof IllegalArgumentException) {
                        return handleBadRequest(error.getMessage());
                    }
                    return handleInternalError(error);
                });
    }
    
    /**
     * Endpoint GET para obtener un producto por ID
     */
    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        // Aquí irían los casos de uso para obtener el producto
        return ServerResponse.notFound().build();
    }
}
```

## 9. Crear Rutas

Crea un archivo en `infrastructure/entry-points/reactive-web/src/main/java/co/com/base/api/product/`:

```java
package co.com.base.api.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Configuración de rutas para productos
 */
@Configuration
public class ProductRouter {
    
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                .POST("/api/products", handler::create)
                .GET("/api/products/{id}", handler::getById)
                .build();
    }
}
```

## 10. Flujo Completo

El flujo de una solicitud HTTP es:

```
1. Solicitud HTTP llega al ProductHandler.create()
   ↓
2. Handler convierte el body a un objeto Product
   ↓
3. Handler llama a CreateProductUseCase.execute(product)
   ↓
4. UseCase valida los datos
   ↓
5. UseCase llama a ProductGateway.save(product)
   ↓
6. ProductGatewayImpl convierte Product a ProductDocument
   ↓
7. ProductGatewayImpl guarda el documento en MongoDB
   ↓
8. El documento se convierte de nuevo a Product
   ↓
9. El Product se retorna al Handler
   ↓
10. Handler envía una respuesta 201 Created con el producto
```

## Checklist para Nuevo Proyecto

- [ ] Cambiar paquete de `co.com.base` a tu paquete
- [ ] Crear entidades en `domain/model`
- [ ] Crear interfaces gateway en `domain/model/gateways`
- [ ] Crear documentos MongoDB en `infrastructure/driven-adapters/mongo-repository`
- [ ] Crear mappers en `infrastructure/driven-adapters/mongo-repository`
- [ ] Crear repositories en `infrastructure/driven-adapters/mongo-repository`
- [ ] Implementar gateways en `infrastructure/driven-adapters/mongo-repository`
- [ ] Crear casos de uso en `domain/usecase`
- [ ] Crear handlers en `infrastructure/entry-points/reactive-web`
- [ ] Crear routers en `infrastructure/entry-points/reactive-web`
- [ ] Actualizar `application.properties` con datos de conexión MongoDB
- [ ] Compilar con `gradle clean build`
- [ ] Ejecutar con `gradle bootRun`

---

**Nota:** Todos los ejemplos usan Reactor (Mono/Flux) para operaciones reactivas, lo que permite mejor rendimiento con operaciones I/O no bloqueantes.
