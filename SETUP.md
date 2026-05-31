# Proyecto Base - Guía de Configuración

Este es un proyecto base limpio y reutilizable para crear nuevas aplicaciones Spring Boot con arquitectura reactiva y patrón de limpieza.

## Estructura del Proyecto

```
proyecto-base/
├── applications/
│   └── app-service/              # Aplicación principal de Spring Boot
├── domain/
│   ├── model/                    # Modelos y gateways (interfaces)
│   └── usecase/                  # Casos de uso de la aplicación
├── infrastructure/
│   ├── driven-adapters/
│   │   └── mongo-repository/     # Implementación de repositorios MongoDB
│   └── entry-points/
│       └── reactive-web/         # Controladores y handlers HTTP
├── gradle/                       # Gradle wrapper
├── build.gradle                  # Configuración gradle principal
├── gradle.properties             # Propiedades del proyecto
└── settings.gradle               # Configuración de módulos
```

## Clases Base Disponibles

### 1. BaseGateway<T, ID>
Ubicación: `domain/model/src/main/java/co/com/base/model/gateways/BaseGateway.java`

Interfaz genérica que define operaciones CRUD básicas:
- `save(T entity)` - Guardar entidad
- `findById(ID id)` - Buscar por ID
- `update(T entity)` - Actualizar entidad
- `deleteById(ID id)` - Eliminar por ID

**Ejemplo de uso:**
```java
public interface ProductGateway extends BaseGateway<Product, String> {
    // Métodos adicionales específicos si son necesarios
}
```

### 2. BaseUseCase
Ubicación: `domain/usecase/src/main/java/co/com/base/usecase/BaseUseCase.java`

Clase base para casos de uso con:
- Métodos de validación: `validateNotNull()`, `validateNotEmpty()`, `validate()`
- Soporte para extender comportamiento común en casos de uso

**Ejemplo de uso:**
```java
@Service
public class CreateProductUseCase extends BaseUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateProductUseCase.class);

    public Mono<Product> execute(Product product) {
        validateNotNull(product, "El producto no puede ser nulo");
        logger.info("Creando producto: {}", product.getId());
        // Lógica del caso de uso
        return Mono.just(product);
    }
}
```

### 3. BaseHandler
Ubicación: `infrastructure/entry-points/reactive-web/src/main/java/co/com/base/api/BaseHandler.java`

Clase base para handlers HTTP con métodos de respuesta estandarizados:
- `handleSuccess(Object data)` - Respuesta 200 OK
- `handleCreated(Object data)` - Respuesta 201 Created
- `handleNoContent()` - Respuesta 204 No Content
- `handleBadRequest(String message)` - Respuesta 400
- `handleNotFound(String message)` - Respuesta 404
- `handleInternalError(Throwable error)` - Respuesta 500

**Ejemplo de uso:**
```java
@Component
public class ProductHandler extends BaseHandler {
    private final CreateProductUseCase createProductUseCase;

    public ProductHandler(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }
    
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Product.class)
            .flatMap(product -> createProductUseCase.execute(product))
            .flatMap(this::handleCreated)
            .onErrorResume(error -> handleInternalError(error));
    }
}
```

### 4. BaseMongoGatewayImpl<T, D, R>
Ubicación: `infrastructure/driven-adapters/mongo-repository/src/main/java/co/com/base/mongo/BaseMongoGatewayImpl.java`

Clase base abstracta para implementar gateways con MongoDB Reactivo:
- Implementa operaciones CRUD de `BaseGateway<T, String>`
- Requiere mapear entre documento Mongo y modelo de dominio
- Mantiene la infraestructura separada del caso de uso

**Ejemplo de uso:**
```java
@Repository
public class ProductGatewayImpl extends BaseMongoGatewayImpl<Product, ProductDocument, ProductReactiveRepository> {

    public ProductGatewayImpl(ProductReactiveRepository repository) {
        super(repository);
    }

    @Override
    protected Product toDomain(ProductDocument document) {
        Product product = new Product();
        product.setId(document.getId());
        product.setNombre(document.getNombre());
        return product;
    }

    @Override
    protected ProductDocument toDocument(Product domain) {
        ProductDocument document = new ProductDocument();
        document.setId(domain.getId());
        document.setNombre(domain.getNombre());
        return document;
    }
}
```

### 5. ProductHandler (Ejemplo)
Ubicación: `infrastructure/entry-points/reactive-web/src/main/java/co/com/base/api/ProductHandler.java`

Handler de ejemplo que demuestra el patrón completo fin-a-fin:
- Recibe solicitudes HTTP (request → body)
- Delega en un caso de uso (CreateProductUseCase)
- Maneja errores de forma reactiva
- Retorna respuestas HTTP formateadas

**Patrón arquitectónico:**
```
HTTP Request → ProductHandler → CreateProductUseCase → ProductGateway → MongoDB
```

**Ejemplo de uso (cuando lo actives en tu aplicación):**

1. Marca ProductHandler como `@Component`:
```java
@Component  // <-- Descomenta para activar
public class ProductHandler extends BaseHandler {
    // ... código
}
```

2. Inyéctalo en ApiRouterConfig:
```java
@Configuration
public class ApiRouterConfig {
    private final ProductHandler productHandler;
    
    public ApiRouterConfig(ProductHandler productHandler) {
        this.productHandler = productHandler;
    }
    
    @Bean
    public RouterFunction<ServerResponse> apiRoutes() {
        return RouterFunctions.route(...)
            .andRoute(POST("/products"), productHandler::create)
            .andRoute(GET("/products/{id}"), productHandler::getById);
    }
}
```

3. Proporciona el gateway impl (CreateProductUseCase lo necesita):
```java
@Service
public class ProductGatewayImpl extends BaseMongoGatewayImpl<Product, ProductDocument, ProductReactiveRepository> {
    // ... mapeos toDomain/toDocument
}

@Service
public class CreateProductUseCase extends BaseUseCase {
    public CreateProductUseCase(ProductGateway gateway) { ... }
    public Mono<Product> execute(Product product) { ... }
}
```

### 6. ApiRouterConfig (Composición)
Ubicación: `infrastructure/entry-points/reactive-web/src/main/java/co/com/base/api/ApiRouterConfig.java`

Clase de configuración que expone las rutas HTTP reactivas usando `RouterFunction<ServerResponse>`.

**Estructura:**
```java
@Configuration
public class ApiRouterConfig {
    
    // TODO: Inyecta handlers aquí cuando necesites rutas de negocio
    // private final ProductHandler productHandler;
    
    @Bean
    public RouterFunction<ServerResponse> apiRoutes() {
        // Ruta de health check
        return RouterFunctions.route(GET("/health"), request -> 
                ServerResponse.ok()
                    .bodyValue(Map.of("status", "UP"))
            );
        
        // Cuando tengas handlers, agrégalos así:
        // .andRoute(POST("/products"), productHandler::create)
        // .andRoute(GET("/products/{id}"), productHandler::getById);
    }
}
```

**Principios:**
- Las rutas de negocio **deben delegar en handlers** (preserva arquitectura)
- Los handlers invoquen **casos de uso** (no repositorios directamente)
- Los casos de uso usan **gateways** para acceder a datos

## Configuración: application.yml

El proyecto utiliza **YAML** (no properties) para la configuración de Spring Boot. YAML es más legible y escalable que el formato properties.

**Ubicación:** `applications/app-service/src/main/resources/application.yml`

### Ejemplo Completo
```yaml
spring:
  application:
    name: proyecto-base
  data:
    mongodb:
      # URI de conexión MongoDB
      uri: mongodb://localhost:27017/proyecto-base
      # Alternativa: configuración granular
      # host: localhost
      # port: 27017
      # database: proyecto-base

server:
  # Puerto del servidor
  port: 8081

logging:
  level:
    # Debug para ver todas las consultas MongoDB
    org.springframework.data.mongodb.core.MongoTemplate: DEBUG
    # Nivel general
    root: INFO
```

### Configuración para Desarrollo

**Sin MongoDB (descomenta la URI):**
```yaml
spring:
  data:
    mongodb:
      # uri: mongodb://localhost:27017/proyecto-base  # Comentado
```

**Con Docker Compose:**
```bash
docker-compose up -d
```

**Con Docker Manual:**
```bash
docker run --name mongodb -d -p 27017:27017 mongo:latest
```

**Con MongoDB local (macOS con Homebrew):**
```bash
brew install mongodb-community
brew services start mongodb-community
```

### Propiedades Comunes

| Propiedad | Uso | Valor Ejemplo |
|-----------|-----|-------|
| `spring.application.name` | Nombre de la app (logs, métricos) | `proyecto-base` |
| `spring.data.mongodb.uri` | Conexión (URI completa) | `mongodb://localhost:27017/db` |
| `server.port` | Puerto del servidor | `8081` |
| `logging.level.*` | Nivel de logs por paquete | `DEBUG`, `INFO`, `WARN` |

## Cambiar el Nombre del Paquete

Si deseas reutilizar este proyecto como base, sigue estos pasos:

### Paso 1: Actualizar gradle.properties
Edita `gradle.properties` y cambia:
```properties
package=co.com.tuempresa.tuproyecto
```

### Paso 2: Reemplazar paquetes en archivos Java
Ejecuta en la terminal desde la raíz del proyecto:

```bash
# En macOS/Linux
find . -type f -name "*.java" -exec sed -i '' 's/co\.com\.base/co.com.tuempresa.tuproyecto/g' {} \;

# En Linux (sin la 's' después de -i)
find . -type f -name "*.java" -exec sed -i 's/co\.com\.base/co.com.tuempresa.tuproyecto/g' {} \;
```

### Paso 3: Reorganizar directorios de paquetes
```bash
# Cambiar estructura de directorios co/com/base a co/com/tuempresa/tuproyecto
find . -type d -path "*/co/com/base" -exec sh -c \
  'parent=$(dirname "$1"); mv "$1" "$parent/tuempresa" 2>/dev/null || true' _ {} \;

# Luego crear la estructura dentro de tuempresa
mkdir -p co/com/tuempresa/tuproyecto
# Y reorganizar los archivos manualmente o con script adicional
```

### Paso 4: Actualizar settings.gradle y otros archivos
- Cambiar `rootProject.name` en `settings.gradle`
- Actualizar `spring.application.name` en `application.properties`
- Actualizar referencias en `build.gradle` si es necesario

### Paso 5: Compilar y validar
```bash
./gradlew clean build
```

## Estructura de Módulos

### App Service (applications/app-service)
Módulo principal que expone la aplicación Spring Boot.
- Configura componentes scan
- Define puntos de entrada
- Gestiona propiedades de aplicación

### Domain Model (domain/model)
Define las entidades y contratos (interfaces):
- Modelos de datos
- Gateways (interfaces de repositorios)

### Domain UseCase (domain/usecase)
Contiene la lógica de negocio:
- Casos de uso reactivos
- Validaciones y reglas de negocio
- Independientes de la infraestructura

### Infrastructure - Mongo Repository
Implementaciones de gateways:
- Mappers (conversión entre modelos y documentos MongoDB)
- Implementaciones de gateways
- Configuración de MongoDB reactiva

### Infrastructure - Reactive Web
Puntos de entrada HTTP:
- Handlers (procesadores de solicitudes)
- Rutas reactivas
- Configuración de seguridad y CORS si es necesario

## Archivos de Configuración

### application.properties
Ubicación: `applications/app-service/src/main/resources/application.properties`

Configuraciones recomendadas:
```properties
spring.application.name=proyecto-base
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/mi-base-de-datos
```

### build.gradle (raíz)
Define propiedades comunes, dependencias y configuración gradle.

### gradle.properties
Define versiones y características del proyecto:
- `package` - Paquete base del proyecto (cambiar esto es lo más importante)
- `reactive=true` - Habilita soporte reactivo
- `lombok=true` - Incluye Project Lombok

## Dependencias Principales

- **Spring Boot** - Framework
- **Spring WebFlux** - API reactiva
- **Spring Data MongoDB Reactive** - MongoDB reactivo
- **Project Lombok** - Generación de código
- **Reactor** - Librería reactiva

## Próximos Pasos para Nuevo Proyecto

1. Cambiar el nombre del paquete (ver sección anterior)
2. Crear entidades en `domain/model`
3. Crear gateways para cada entidad en `domain/model/gateways`
4. Implementar casos de uso en `domain/usecase`
5. Crear mappers en `infrastructure/driven-adapters/mongo-repository`
6. Implementar gateways en `infrastructure/driven-adapters/mongo-repository`
7. Crear handlers en `infrastructure/entry-points/reactive-web`
8. Configurar rutas en handlers

## Compilación y Ejecución

```bash
# Limpiar y compilar
./gradlew clean build

# Ejecutar tests
./gradlew test

# Ejecutar aplicación
./gradlew bootRun

# Crear JAR
./gradlew build
```

## Notas Importantes

- El proyecto usa **reactividad** con Mono/Flux de Project Reactor
- Todos los casos de uso deben extender `BaseUseCase`
- Todos los handlers deben extender `BaseHandler`
- Los gateways deben implementar `BaseGateway<T, ID>`
- Usa MongoDB por defecto, cambia según necesites en `build.gradle`

---

**Última actualización:** Febrero 2026
