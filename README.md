# Proyecto Base - Arquitectura Limpia con Spring Boot Reactivo

Proyecto base limpio y reutilizable para crear aplicaciones empresariales modernas con **Spring Boot**, **WebFlux** (reactivo), **MongoDB** y arquitectura de **Limpieza Hexagonal**.

## 📋 Características Principales

- ✅ **Arquitectura Limpia**: Separación clara entre capas de negocio, aplicación e infraestructura
- ✅ **Reactivo**: Usa Project Reactor (Mono/Flux) para operaciones no bloqueantes
- ✅ **Modular**: Estructura basada en módulos Gradle independientes
- ✅ **MongoDB Reactivo**: Soporte completo de bases de datos NoSQL reactivas
- ✅ **Spring Boot 3.x**: Framework moderno y actualizado
- ✅ **Lombok**: Generación automática de getters, setters y otros
- ✅ **Listo para Producción**: Configuración base lista para escalabilidad

## 🚀 Inicio Rápido

### Requisitos

- Java 17 o superior
- Gradle 8.0+
- MongoDB 5.0+ (local o remoto)

### Compilar y Ejecutar

```bash
# Limpiar y compilar
./gradlew clean build

# Ejecutar la aplicación
./gradlew bootRun

# Ejecutar tests
./gradlew test
```

## 🔑 Pasos Iniciales

### 1. Cambiar el Paquete Base (IMPORTANTE)

Este es el paso más importante para reutilizar el proyecto.

**Opción A: Script automatizado (recomendado)**

```bash
# En macOS/Linux
find . -type f -name "*.java" -exec sed -i '' \
  's/co\.com\.base/co.com.tuempresa.tuproyecto/g' {} \;

# En Linux (sin la 's' después de -i)
find . -type f -name "*.java" -exec sed -i \
  's/co\.com\.base/co.com.tuempresa.tuproyecto/g' {} \;
```

**Opción B: Manual**

1. Editar `gradle.properties`: cambiar `package=co.com.store`
2. Reemplazar en todos los archivos `.java` manualmente
3. Reorganizar directorios

Ver [SETUP.md](SETUP.md) para instrucciones detalladas.

### 2. Actualizar Configuraciones

```bash
# gradle.properties
package=co.com.tuempresa.tuproyecto

# settings.gradle
rootProject.name = 'mi-proyecto'

# application.properties
spring.application.name=mi-proyecto
spring.data.mongodb.uri=mongodb://localhost:27017/mi-proyecto
```

## 📚 Documentación Completa

- **[SETUP.md](SETUP.md)** - Guía completa de configuración y estructura del proyecto
- **[EJEMPLOS.md](EJEMPLOS.md)** - Ejemplos paso a paso de implementación con código real

## 🏗️ Estructura del Proyecto

```
proyecto-base/
├── applications/
│   └── app-service/              # Aplicación principal Spring Boot
├── domain/                        # Capa de Dominio
│   ├── model/                    # Entidades y contratos
│   └── usecase/                  # Casos de uso (lógica de negocio)
├── infrastructure/               # Capa de Infraestructura
│   ├── driven-adapters/
│   │   └── mongo-repository/     # Implementación con MongoDB
│   └── entry-points/
│       └── reactive-web/         # Controladores HTTP reactivos
├── SETUP.md                      # Guía de configuración
├── EJEMPLOS.md                   # Ejemplos prácticos
└── README.md                     # Este archivo
```

## 🔧 Clases Base Disponibles

### BaseGateway<T, ID>

```java
// En domain/model/gateways/
public interface ProductGateway extends BaseGateway<Product, String> {
}
```

Operaciones CRUD automáticas: save, findById, update, deleteById

### BaseUseCase

```java
// En domain/usecase/
@Service
public class CreateProductUseCase extends BaseUseCase {
    public Mono<Product> execute(Product product) {
        validateNotNull(product, "Product cannot be null");
        // Lógica de negocio
    }
}
```

### BaseHandler

```java
// En infrastructure/entry-points/reactive-web/
@Component
public class ProductHandler extends BaseHandler {
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Product.class)
            .flatMap(p -> createUseCase.execute(p))
            .flatMap(this::handleCreated);
    }
}
```

## 📖 Ejemplos de Uso

Para ejemplos completos paso a paso, consulta [EJEMPLOS.md](EJEMPLOS.md):

1. Crear una entidad (modelo)
2. Crear un gateway (interfaz de repositorio)
3. Crear documentos MongoDB
4. Crear mappers
5. Implementar gateways
6. Crear casos de uso
7. Crear handlers (endpoints)
8. Configurar rutas

## ⚙️ Configuración

### application.properties

```properties
spring.application.name=proyecto-base
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/proyecto-base
logging.level.org.springframework.data.mongodb=DEBUG
```

### gradle.properties

```properties
package=co.com.store                # Cambiar a tu paquete
reactive=true                      # Habilitar WebFlux
lombok=true                        # Incluir Lombok
org.gradle.parallel=true           # Compilación paralela
```

## 🔄 Arquitectura de Capas

```
HTTP Request → Handler → UseCase → Gateway → Repository → MongoDB
     ↓            ↓         ↓         ↓         ↓          ↓
  Entrada    Valida      Negocios   Contrato   Datos    Persistencia
   (API)    Entrada                  (I)      (I)
```

- **Handler**: Recibe HTTP, deserializa, delega
- **UseCase**: Lógica de negocio, validaciones
- **Gateway**: Interfaz de persistencia (contrato)
- **Repository**: Implementación real (MongoDB)
- **Mapper**: Conversión entre capas

## 📦 Tecnologías Principales

- **Spring Boot 3.x** - Framework principal
- **Spring WebFlux** - API reactiva
- **Spring Data MongoDB Reactive** - Base de datos reactiva
- **Project Reactor** - Programación reactiva (Mono/Flux)
- **Lombok** - Generación automática de código
- **Gradle 8.0+** - Build automation
- **Java 17+** - Runtime

## 🛠️ Flujo de Desarrollo

1. **Definir Modelos** en `domain/model/`
2. **Crear Gateways** en `domain/model/gateways/`
3. **Implementar MongoDB** en `infrastructure/driven-adapters/mongo-repository/`
4. **Crear Casos de Uso** en `domain/usecase/`
5. **Exposer API** en `infrastructure/entry-points/reactive-web/`

## 🐛 Solución de Problemas

| Problema                              | Solución                                     |
| ------------------------------------- | -------------------------------------------- |
| "package co.com.store does not exist" | Cambiar paquete correctamente (ver SETUP.md) |
| MongoDB no conecta                    | Verificar que MongoDB esté ejecutándose      |
| Gradle wrapper error                  | `gradle wrapper --gradle-version=8.2.1`      |
| Build fail con Lombok                 | Asegurar `lombok=true` en gradle.properties  |

## 💡 Buenas Prácticas

✅ **Usa:**

- Extender BaseGateway, BaseUseCase, BaseHandler
- Tipos reactivos (Mono/Flux)
- Validaciones en casos de uso
- Mappers para conversión entre capas
- Inyección de dependencias

❌ **Evita:**

- Lógica de negocio en handlers
- Exponer documentos MongoDB directamente
- I/O bloqueante en reactivo
- Acceso directo a repositorios desde handlers

## 📄 Información

**Última actualización:** Febrero 2026
**Versión:** 1.0 Base

---

**📖 Para más información:**

- [SETUP.md](SETUP.md) - Guía de configuración detallada
- [EJEMPLOS.md](EJEMPLOS.md) - Ejemplos de código completo

**🚀 ¡Listo para empezar!** Sigue los pasos en "Pasos Iniciales" arriba.

## Clases Base Disponibles

- **BaseGateway<T, ID>** - Interface para repositorios
- **BaseUseCase** - Clase base para casos de uso
- **BaseHandler** - Clase base para handlers HTTP

## Stack Tecnológico

- Java 17+
- Spring Boot 3.x
- Spring WebFlux (Reactivo)
- Spring Data MongoDB Reactivo
- Project Lombok
- Gradle

## Documentación

Ver [SETUP.md](SETUP.md) para guía completa de configuración y desarrollo.
