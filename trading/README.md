# Trading Journal Application

Una aplicación Spring Boot para gestionar un diario de operaciones de trading, permitiendo registrar, actualizar y analizar operaciones comerciales así como administrar cuentas de trading.

## 📋 Descripción del Proyecto

Este proyecto es una API REST desarrollada con Spring Boot 3.5.6 que proporciona funcionalidades para:

- **Gestión de Cuentas**: Crear, actualizar y consultar cuentas de trading
- **Gestión de Operaciones**: Registrar operaciones (trades), actualizar su estado y obtener resúmenes
- **Análisis de Datos**: Calcular métricas y resúmenes de operaciones
- **Documentación Interactiva**: API documentada con Swagger/OpenAPI

## 🛠️ Tecnología

- **Java 25**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - ORM
- **MySQL 8.0** - Base de datos
- **Lombok** - Generación de código
- **SpringDoc OpenAPI** - Documentación de API
- **Spring Validation** - Validación de datos
- **Maven** - Gestor de dependencias

## 📦 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/trading/trading/
│   │   ├── controllers/        # Controladores REST
│   │   ├── services/           # Lógica de negocio
│   │   ├── models/             # Entidades JPA
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── mappers/            # Convertidores de entidades
│   │   ├── repositories/       # Acceso a datos
│   │   ├── exceptions/         # Manejo de excepciones
│   │   └── scheduling/         # Tareas programadas
│   └── resources/
│       ├── application.properties
│       └── data/trades.csv     # Datos de ejemplo
└── test/
    └── java/com/example/trading/trading/  # Tests unitarios
```

## 🚀 Inicio Rápido

### Requisitos Previos

- Java 25 instalado
- MySQL 8.0 en ejecución
- Maven 3.8+

### Instalación

1. **Clona o descarga el proyecto**:

   ```bash
   cd trading
   ```

2. **Configura la base de datos** en `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tradingjournaldb
   spring.datasource.username=root
   spring.datasource.password=tu_contraseña
   ```

3. **Compila y ejecuta el proyecto**:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   O ejecuta el JAR compilado:

   ```bash
   java -jar target/trading-0.0.1-SNAPSHOT.jar
   ```

## 📡 API Endpoints

La API corre en `http://localhost:9090`

### Documentación Interactiva

- **Swagger UI**: `http://localhost:9090/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:9090/v3/api-docs`

### Operaciones Principales

#### Cuentas

- `GET /api/accounts` - Listar todas las cuentas
- `GET /api/accounts/{id}` - Obtener una cuenta
- `POST /api/accounts` - Crear una nueva cuenta
- `PUT /api/accounts/{id}` - Actualizar una cuenta
- `DELETE /api/accounts/{id}` - Eliminar una cuenta

#### Operaciones (Trades)

- `GET /api/trades` - Listar todos los trades
- `GET /api/trades/{id}` - Obtener un trade
- `POST /api/trades` - Crear un nuevo trade
- `PUT /api/trades/{id}` - Actualizar un trade
- `DELETE /api/trades/{id}` - Eliminar un trade
- `GET /api/trades/summary` - Obtener resumen de operaciones

## 🧪 Tests

Ejecuta los tests con Maven:

```bash
mvn test
```

### Tests Disponibles

- `AccountControllerTest` - Tests del controlador de cuentas
- `TradeControllerTest` - Tests del controlador de trades
- `AccountServiceTest` - Tests del servicio de cuentas
- `TradeServiceTest` - Tests del servicio de trades
- DTOs Tests - Validación de objetos de transferencia

## 🔄 Tareas Programadas

### FixTradesStatusSchedule

Tarea automática que ajusta el estado de los trades según reglas de negocio definidas.

## 🚨 Manejo de Excepciones

La aplicación incluye un manejador global de excepciones:

- `ResourceNotFoundException` - Recurso no encontrado (404)
- `BusinessRuleException` - Violación de reglas de negocio
- `MethodArgumentNotValidException` - Errores de validación
- `Exception` - Excepciones genéricas
