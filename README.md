# 🏪 Store API - Gestión Reactiva de Tiendas y Productos

¡Bienvenido a **Store API**! Este proyecto es un backend moderno y altamente escalable diseñado para gestionar Tiendas, Productos y Categorías. 

Está construido utilizando los estándares más altos de la industria del software: **Arquitectura Limpia (Hexagonal)** y **Programación Reactiva**, lo que significa que es capaz de manejar miles de peticiones simultáneas sin bloquear el servidor.

---

## 📋 ¿Qué hace esta API?

Esta API permite administrar un catálogo comercial con las siguientes características:
- **Categorías:** Crear y listar agrupaciones (ej. Electrónica, Hogar).
- **Tiendas:** Administrar sucursales comerciales.
- **Productos:** Crear productos vinculados inteligentemente a una Tienda y a una Categoría.
- **Búsqueda Avanzada:** Filtrar productos en tiempo real por nombre, por tienda o por categoría.

Todo esto respondiendo a la velocidad de la luz gracias a su naturaleza asíncrona.

---

## 🛠️ Tecnologías Utilizadas

Este proyecto utiliza un stack tecnológico moderno y robusto:

- **Java 17:** Lenguaje de programación principal (Versión LTS estable).
- **Spring Boot 3.x:** Framework para simplificar la configuración del servidor.
- **Spring WebFlux:** Motor de programación reactiva (operaciones no bloqueantes usando `Mono` y `Flux`).
- **MongoDB Reactivo:** Base de datos NoSQL ultra rápida y flexible.
- **Gradle 8.0+:** Herramienta de automatización de compilación estructurada en múltiples módulos.
- **Lombok:** Librería para reducir código repetitivo (getters, setters, constructores).

---

## 🚀 Inicio Rápido (Cómo ejecutar el proyecto)

Si es tu primera vez abriendo este proyecto, sigue estos sencillos pasos para verlo funcionar en tu máquina local.

### 1. Requisitos Previos
Asegúrate de tener instalado en tu computadora:
- Java 17 o superior.
- Una base de datos **MongoDB** corriendo localmente en el puerto por defecto (`localhost:27017`).

### 2. Compilar el Proyecto
Abre tu terminal en la raíz del proyecto y ejecuta el siguiente comando para descargar las dependencias y compilar el código:

**En Windows:**
```cmd
.\gradlew clean build
```

**En Mac/Linux:**


```cmd
./gradlew clean build
```
*(Nota: El primer build puede tardar un poco mientras descarga las librerías necesarias).*

**3. Encender el Servidor**

Una vez compilado exitosamente, levanta la aplicación con este comando:

**En Windows:**

```cmd
.\gradlew bootRun
```

**En Mac/Linux:**

```cmd
./gradlew bootRun
```

¡Listo! Tu API estará corriendo en http://localhost:8081.

**🗺️ Entendiendo la Arquitectura (Clean Architecture)**

Este proyecto no mezcla todo el código en un solo lugar. Está dividido en **capas (módulos)** para que sea fácil de mantener y escalar:

1. **domain/model (El Corazón):** Aquí viven nuestras entidades puras de Java (Product, Store, Category). No saben nada de bases de datos ni de internet.
1. **domain/usecase (Las Reglas):** Aquí están las reglas del negocio. Validan que los precios no sean negativos y aseguran que los productos se guarden con las tiendas y categorías correctas.
1. **infrastructure/driven-adapters (Conexión a BD):** Aquí le enseñamos a la aplicación cómo guardar los datos específicamente en MongoDB.
1. **infrastructure/entry-points (Las Puertas de Entrada):** Aquí están los *Routers* y *Handlers* que exponen nuestras URLs (/apistore/v1/...) al mundo exterior.
1. **applications/app-service (El Ensamblador):** El punto de arranque que une todas las capas anteriores y enciende Spring Boot.

**📍 Endpoints Principales (Rutas)**

Todas las rutas de negocio están prefijadas con /apistore/v1/. Aquí tienes algunos ejemplos que puedes probar en Postman:

- **Prueba de vida:** GET http://localhost:8081/health
- **Categorías:** GET / POST / PUT / DELETE a /apistore/v1/categories
- **Tiendas:** GET / POST / PUT / DELETE a /apistore/v1/stores
- **Productos:** GET / POST / PUT / DELETE a /apistore/v1/products

**Ejemplos de Filtros Reactivos:**

- Buscar producto por nombre: GET /apistore/v1/products?name=laptop
- Filtrar por tienda: GET /apistore/v1/products?storeId=ID\_DE\_LA\_TIENDA
- Filtrar por categoría: GET /apistore/v1/products?categoryId=ID\_DE\_LA\_CATEGORIA

**🐛 Solución de Problemas Comunes**

|**Problema**|**Solución**|
| :- | :- |
|**Error conectando a MongoDB**|Verifica que tu servicio de MongoDB esté encendido y usando el puerto 27017.|
|**CommandNotFoundException (gradlew)**|Estás intentando usar gradlew sin el prefijo. Usa .\gradlew en Windows o ./gradlew en Linux/Mac.|
|**Error de versión de Java**|Verifica que la variable de entorno JAVA\_HOME apunte a tu instalación de Java 17.|

*Desarrollado aplicando los principios de la Arquitectura Hexagonal y Programación Reactiva.*

