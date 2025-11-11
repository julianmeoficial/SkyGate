# SkyGate Backend - Documentación 

## Introducción

Sistema backend para la gestion automatizada de gates aeroportuarios basado en un Automata Finito Determinista (DFA). El sistema controla la detección de aeronaves, asignación dinámica de gates, seguimiento de estados, y comunicación en tiempo real con el frontend mediante WebSocket.

---

## Stack Tecnológico

### Framework y Core
- **Spring Boot 3.5.6**: Framework principal del backend.
- **Java 17**: Version del lenguaje de programación.
- **Maven**: Gestor de dependencias y construcción del proyecto.

### Persistencia de Datos
- **Spring Data JPA**: Capa de abstracción para acceso a datos.
- **Hibernate**: Implementación de JPA.
- **PostgreSQL**: Base de datos principal en producción.
- **H2 Database**: Base de datos en memoria para desarrollo y testing.

### Seguridad
- **Spring Security**: Framework de seguridad y autenticación.
- **JWT (JSON Web Tokens)**: Sistema de autenticación basado en tokens.
  - `jjwt-api`, `jjwt-impl`, `jjwt-jackson` v0.11.5.
- **OAuth2 Resource Server**: Soporte para integración OAuth2.
- **BCrypt**: Algoritmo de encriptación de contraseñas.

### Comunicación en Tiempo Real
- **WebSocket (STOMP)**: Protocolo de comunicación bidireccional.
- **SimpMessagingTemplate**: Mensajería para notificaciones en tiempo real.

### Comunicación con Hardware (MQTT)
- **Spring Integration MQTT**: Integración con protocolo MQTT.
- **Eclipse Paho Client v1.2.5**: Cliente MQTT para comunicación con Arduino/ESP32.
- **RabbitMQ (AMQP)**: Sistema de mensajería asíncrona (es opcional).

### Documentación y Monitoring
- **SpringDoc OpenAPI v2.7.0**: Generación automática de documentación API.
- **Swagger UI**: Interfaz grafica para pruebas de endpoints.
- **Spring Boot Actuator**: Endpoints de monitoreo y salud del sistema.

### Validación y Serialización
- **Spring Validation**: Validación de datos con anotaciones Jakarta.
- **Jackson Databind**: Serialización/deserialización JSON.
- **Jackson JSR310**: Soporte para tipos de fecha/hora de Java 8+.

### Desarrollo
- **Lombok**: Reducción de boilerplate code
- **Spring DevTools**: Recarga automática en desarrollo

---

## Arquitectura del Proyecto

### Estructura de Paquetes

```
com.skygate.backend
│
├── BackendApplication.java (Main application class)
│
├── config/
│   ├── AppConfig.java                  [Configuracion general de la aplicacion]
│   ├── AsyncConfig.java                [Configuracion de procesamiento asincrono]
│   ├── DatabaseConfig.java             [Configuracion de conexion a base de datos]
│   ├── DataInitializer.java            [Inicializacion de datos por defecto]
│   ├── JacksonConfig.java              [Configuracion de serializacion JSON]
│   ├── MqttConfig.java                 [Configuracion de broker MQTT (futuro)]
│   ├── SchedulingConfig.java           [Configuracion de tareas programadas]
│   ├── SecurityConfig.java             [Configuracion de seguridad y CORS]
│   ├── SwaggerConfig.java              [Configuracion de documentacion OpenAPI]
│   ├── WebConfig.java                  [Configuracion de endpoints REST]
│   └── WebSocketConfig.java            [Configuracion de conexiones WebSocket]
│
├── controller/
│   ├── AssignmentController.java       [Endpoints de asignaciones gate-vuelo]
│   ├── FlightController.java           [Endpoints de gestion de vuelos]
│   ├── GateController.java             [Endpoints de gestion de gates]
│   └── MonitoringController.java       [Endpoints de monitoreo del sistema]
│
├── event/
│   └── GateFreedEvent.java             [Evento publicado cuando un gate se libera]
│
├── exception/
│   ├── AircraftNotFoundException.java
│   ├── AssignmentNotFoundException.java
│   ├── ErrorResponse.java              [Estructura de respuesta de errores]
│   ├── FlightNotFoundException.java
│   ├── GateAlreadyOccupiedException.java
│   ├── GateNotFoundException.java
│   ├── GlobalExceptionHandler.java     [Manejo centralizado de excepciones]
│   ├── InvalidFlightDataException.java
│   ├── InvalidStateTransitionException.java
│   └── NoAvailableGateException.java
│
├── listener/
│   └── GateFreedEventListener.java     [Escucha eventos de liberacion de gates]
│
├── model/
│   ├── dto/
│   │   ├── request/
│   │   │   ├── AircraftRequestDTO.java
│   │   │   ├── AutomataInputRequestDTO.java
│   │   │   ├── FlightDetectionRequestDTO.java
│   │   │   ├── FlightRequestDTO.java
│   │   │   ├── FlightStatusUpdateRequestDTO.java
│   │   │   ├── GateAssignmentRequestDTO.java
│   │   │   ├── GateRequestDTO.java
│   │   │   └── GateStatusUpdateRequestDTO.java
│   │   │
│   │   └── response/
│   │       ├── AircraftResponseDTO.java
│   │       ├── AircraftSimpleDTO.java
│   │       ├── ApiResponseDTO.java
│   │       ├── AssignmentResponseDTO.java
│   │       ├── AutomataTransitionResponseDTO.java
│   │       ├── FlightResponseDTO.java
│   │       ├── FlightSimpleDTO.java
│   │       ├── GateResponseDTO.java
│   │       └── GateSimpleDTO.java
│   │
│   ├── entity/
│   │   ├── Aircraft.java               [Entidad: Informacion de aeronaves]
│   │   ├── Assignment.java             [Entidad: Relacion vuelo-gate]
│   │   ├── Flight.java                 [Entidad: Informacion de vuelos]
│   │   └── Gate.java                   [Entidad: Informacion de gates]
│   │
│   └── enums/
│       ├── AircraftType.java           [Tipos: NARROW_BODY, WIDE_BODY, REGIONAL]
│       ├── AutomataInput.java          [Inputs del automata: I1-I6]
│       ├── AutomataOutput.java         [Outputs del automata: O1-O5]
│       ├── AutomataState.java          [Estados del DFA: S0-S7]
│       ├── FlightStatus.java           [Estados de vuelo]
│       ├── GateStatus.java             [Estados de gate: FREE, ASSIGNED, OCCUPIED]
│       └── GateType.java               [Tipos de gate segun capacidad]
│
├── mqtt/
│   ├── MqttHealthIndicator.java        [Indicador de salud de conexion MQTT]
│   ├── MqttMessageHandler.java         [Procesador de mensajes MQTT entrantes]
│   ├── MqttPublisher.java              [Publicador de mensajes MQTT]
│   └── MqttSubscriber.java             [Suscriptor a topicos MQTT]
│
├── repository/
│   ├── AircraftRepository.java         [Repositorio JPA para Aircraft]
│   ├── AssignmentRepository.java       [Repositorio JPA para Assignment]
│   ├── FlightRepository.java           [Repositorio JPA para Flight]
│   └── GateRepository.java             [Repositorio JPA para Gate]
│
├── security/
│   ├── AuthenticationController.java   [Endpoints de login/register]
│   ├── CustomUserDetailsService.java   [Carga de usuarios para autenticacion]
│   ├── JwtAuthenticationFilter.java    [Filtro de validacion de tokens JWT]
│   ├── JwtTokenProvider.java           [Generacion y validacion de tokens]
│   └── SecurityUtils.java              [Utilidades de seguridad]
│
├── service/
│   ├── assignment/
│   │   ├── AssignmentService.java      [Logica de asignaciones gate-vuelo]
│   │   └── AssignmentStrategyService.java [Estrategias de asignacion dinamica]
│   │
│   ├── automata/
│   │   ├── AutomataService.java        [Servicio principal del DFA]
│   │   ├── AutomataStateManager.java   [Gestion de estados del automata]
│   │   ├── StateTransitionService.java [Transiciones entre estados]
│   │   └── WaitingFlightProcessor.java [Procesamiento de vuelos en espera]
│   │
│   ├── flight/
│   │   ├── FlightDetectionService.java [Deteccion de aeronaves entrantes]
│   │   └── FlightService.java          [CRUD y logica de vuelos]
│   │
│   ├── gate/
│   │   ├── GateAvailabilityService.java [Verificacion de disponibilidad]
│   │   └── GateService.java            [CRUD y logica de gates]
│   │
│   └── hardware/
│       ├── HardwareService.java        [Integracion con Arduino/ESP32]
│       └── LedControlService.java      [Control de LEDs indicadores]
│
├── util/
│   ├── Constants.java                  [Constantes de la aplicacion]
│   ├── DateUtil.java                   [Utilidades de fecha/hora]
│   └── ValidationUtil.java             [Utilidades de validacion]
│
└── websocket/
    ├── WebSocketController.java        [Controlador de mensajes WebSocket]
    ├── WebSocketEventListener.java     [Escucha eventos de conexion/desconexion]
    ├── WebSocketEventPublisher.java    [Publicador de eventos por WebSocket]
    └── WebSocketHandler.java           [Manejador personalizado de WebSocket]
```

---

## Descripción de Paquetes

### config
Contiene todas las clases de configuración del sistema. Define beans, comportamientos de Spring, conexiones a bases de datos, seguridad, CORS, WebSocket, MQTT y serialización JSON.

### controller
Endpoints REST que exponen la API del sistema. Maneja solicitudes HTTP para vuelos, gates, asignaciones y monitoreo. Aplica validaciones y delega lógica a los servicios.

### event
Define eventos del dominio que son publicados cuando ocurren acciones importantes (por ejemplo, liberación de un gate).

### exception
Manejo centralizado de excepciones personalizadas. GlobalExceptionHandler captura todas las excepciones y genera respuestas HTTP consistentes.

### listener
Escuchadores de eventos que reaccionan automáticamente a cambios en el sistema (por ejemplo, reasignación automática cuando se libera un gate).

### model/dto
Data Transfer Objects para solicitudes (request) y respuestas (response). Separación entre la representación de datos en la API y las entidades de base de datos.

### model/entity
Entidades JPA que representan las tablas de la base de datos. Incluyen relaciones entre vuelos, aeronaves, gates y asignaciones.

### model/enums
Enumeraciones que definen tipos y estados del sistema: estados del automata (S0-S7), inputs (I1-I6), outputs (O1-O5), tipos de aeronaves, estados de vuelos y gates.

### mqtt
Integración con protocolo MQTT para comunicación con hardware Arduino/ESP32. Actualmente configurado pero no utilizado, preparado para desarrollo futuro con sistemas físicos.

### repository
Interfaces JPA Repository para acceso a base de datos. Proporcionan operaciones CRUD y queries personalizadas.

### security
Sistema de autenticación y autorización. Implementa JWT para autenticación stateless, filtros de seguridad, y gestion de usuarios.

### service/assignment
Lógica de asignación de gates a vuelos. Implementa estrategias de asignación dinámica basadas en tipo de aeronave y disponibilidad.

### service/automata
Nucleo del sistema automata DFA. Controla transiciones de estado, válida inputs, genera outputs y mantiene la máquina de estados de cada vuelo.

### service/flight
Gestion de vuelos: detección, creación, actualización y seguimiento de estado.

### service/gate
Gestion de gates: disponibilidad, ocupación, liberación y validación de compatibilidad con tipos de aeronaves.

### service/hardware
Servicios para integración con hardware físico (LED, pantallas, Arduino). Actualmente implementado para desarrollo futuro.

### util
Clases utilitarias con funciones de ayuda: constantes, validaciones y manipulación de fechas.

### websocket
Comunicación en tiempo real con el frontend. Pública actualizaciones de estado del automata, asignaciones, y cambios en vuelos/gates.

---

## Funcionalidades del Sistema

### 1. Automata Finito Determinista (DFA)

El backend implementa un DFA que controla el ciclo de vida de los vuelos:

**Estados (S0-S7)**
- S0: Estado inicial (sistema inactivo).
- S1: Aeronave detectada.
- S2: Tipo de aeronave confirmado.
- S3: Buscando gate disponible.
- S4: Gate asignado.
- S5: Aeronave estacionada.
- S6: Vuelo en espera (sin gate disponible).
- S7: Estado final (aeronave partió).

**Inputs (I1-I6)**
- I1: Detección de aeronave.
- I2: Confirmación de tipo de aeronave.
- I3: Gate disponible encontrado.
- I4: No hay gates disponibles.
- I5: Aeronave arribó al gate.
- I6: Aeronave partió del gate.

**Outputs (O1-O5)**
- O1: Activar LEDs (verde) - gate asignado.
- O2: Notificación de asignación + activar LEDs.
- O3: Activar LEDs (rojo) - gate ocupado.
- O4: Actualizar pantalla de espera.
- O5: Actualizar base de datos.

### 2. Gestión de Vuelos

- Detección automática de aeronaves entrantes.
- Registro de información de vuelo (número, origen, destino, aerolínea).
- Seguimiento de tiempos programados y reales (llegada/salida).
- Transiciones automáticas de estado mediante DFA.
- Historial completo de estados y asignaciones.

### 3. Gestión de Gates

- Control de disponibilidad en tiempo real.
- Clasificación por tipo (NARROW_BODY, WIDE_BODY, REGIONAL).
- Estados: FREE (libre), ASSIGNED (asignado), OCCUPIED (ocupado).
- Validación de compatibilidad con tipo de aeronave.
- Liberación automática al detectar partida.

### 4. Asignación Dinámica

- Algoritmo de asignación basado en tipo de aeronave.
- Búsqueda de gate compatible disponible.
- Sistema de cola para vuelos en espera
- Reasignación automática cuando se libera un gate.
- Prioridad FIFO para vuelos en espera.

### 5. Comunicación en Tiempo Real

**WebSocket**
- Notificaciones instantáneas de cambios de estado.
- Actualizaciones de asignaciones en vivo.
- Transiciones del automata en tiempo real.
- Eventos de conexión/desconexión de clientes.

**Tópicos WebSocket**
- `/topic/automata/transitions`: Transiciones del DFA.
- `/topic/gates`: Actualizaciones de gates.
- `/topic/flights`: Actualizaciones de vuelos.
- `/topic/assignments`: Asignaciones nuevas/modificadas.

### 6. Seguridad y Autenticación

- Autenticación basada en JWT.
- Tokens de acceso con expiración configurable.
- Encriptación de contraseñas con BCrypt.
- Protección de endpoints mediante Spring Security.
- CORS configurado para integración con frontend.

### 7. Monitoreo y Salud

- Endpoints de Actuator para health checks.
- Métricas de rendimiento del sistema.
- Estado de conexiones (DB, MQTT, WebSocket).
- Logs detallados de transiciones y operaciones.

---

## Flujo de Operación Principal

### Detección y Asignación de Vuelo

1. Sistema detecta aeronave entrante (Input I1).
2. Vuelo transiciona de S0 a S1 (DETECTED).
3. Se confirma tipo de aeronave (Input I2).
4. Sistema busca gate compatible disponible.
5. Si hay gate: Input I3, transición S2 -> S3 -> S4 (GATE_ASSIGNED).
   - Se crea Assignment activo.
   - Gate cambia a ASSIGNED.
   - Se activan LEDs (Output O1, O2).
6. Si no hay gate: Input I4, transición S2 -> S6 (WAITING).
   - Vuelo entra en cola de espera.
   - Se actualiza pantalla (Output O4).

### Arribo de Aeronave

1. Sistema detecta arribo (Input I5)
2. Vuelo transiciona S4 -> S5 (PARKED).
3. Gate cambia a OCCUPIED.
4. Se actualiza tiempo real de llegada.
5. LEDs cambian a rojo (Output O3).

### Partida de Aeronave

1. Sistema detecta partida (Input I6).
2. Vuelo transiciona S5 -> S0 (DEPARTED).
3. Gate se libera (FREE).
4. Assignment se marca como inactivo.
5. Sistema busca vuelos en espera.
6. Si hay vuelos esperando: asignación automática.

---

## Integración con Hardware (MQTT)

### Configuración Actual

El sistema tiene configurado MQTT, pero actualmente no está activo. Está preparado para integración futura con:

- Arduino/ESP32 para control de LED.
- Sensores de detección de aeronaves.
- Pantallas LCD/OLED de información.
- Sistemas de iluminación de gates.

### Arquitectura Prevista

```
Backend (Spring Boot)
    |
    | MQTT
    v
Broker MQTT (Mosquitto/HiveMQ)
    |
    | MQTT
    v
Arduino/ESP32
    |
    v
Hardware Físico (LEDs, Sensores, Pantallas)
```

### Tópicos MQTT Planificados

- `skygate/gates/{gateId}/led`: Control de LED.
- `skygate/gates/{gateId}/status`: Estado del gate.
- `skygate/flights/{flightId}/assignment`: Notificación de asignación.
- `skygate/sensors/detection`: Detección de aeronaves.
- `skygate/displays/main`: Actualización de pantallas.

---

## Mejoras Futuras

### Alta Prioridad

1. **Integración MQTT Completa**
   - Activar comunicación con Arduino/ESP32.
   - Implementar protocolo de mensajes bidireccional.
   - Control real de hardware físico.
   - Sensores de detección automática.

2. **Sistema de Colas Avanzado**
   - Prioridad dinámica basada en retrasos.
   - Asignación inteligente con predicción de tiempos.
   - Optimización de ocupación de gates.
   - Alertas de vuelos críticos.

3. **Dashboard de Monitoreo**
   - Visualización del diagrama del DFA en vivo.
   - Métricas en tiempo real de ocupación.
   - Gráficos de rendimiento histórico.
   - Alertas y notificaciones administrativas.

### Media Prioridad

4. **Sistema de Notificaciones**
   - Email para eventos críticos.
   - Push notifications para retrasos.
   - Notificaciones a personal aeroportuario.

5. **Optimización de Asignaciones**
   - Machine Learning para predicción de tiempos.
   - Algoritmos genéticos para asignación óptima.
   - Consideración de distancias entre gates.

6. **Reportes y Analytics**
   - Generación de reportes PDF/Excel(csv).
   - Estadísticas de uso de gates.
   - Análisis de eficiencia operativa.
   - Comparativas temporales.

### Baja Prioridad

7. **Integración con Sistemas Externos**
   - APIs de aerolíneas (SITA, ARINC).
   - Sistemas de gestion aeroportuaria (AODB).
   - Meteorología para predicciones.
   - Control de trafico aéreo.

8. **Multi-tenant**
   - Soporte para multiples aeropuertos.
   - Configuración independiente por tenant.
   - Aislamiento de datos.
   - Facturación y licenciamiento.

9. **Modo Offline**
   - Operación sin conexión a internet.
   - Sincronización al recuperar conexión.
   - Cache local de datos críticos.
   - Resiliencia ante fallos de red.

10. **Seguridad Avanzada**
    - Autenticación multifactor (MFA).
    - Auditoria completa de operaciones.
    - Encriptación end-to-end para MQTT.
    - Roles y permisos granulares.

---

## API Endpoints

### Autenticación
- POST `/api/v1/auth/login` - Iniciar sesión
- POST `/api/v1/auth/register` - Registrar usuario

### Vuelos
- GET `/api/v1/flights` - Listar todos los vuelos
- GET `/api/v1/flights/{id}` - Obtener vuelo por ID
- POST `/api/v1/flights` - Crear nuevo vuelo
- PUT `/api/v1/flights/{id}` - Actualizar vuelo
- DELETE `/api/v1/flights/{id}` - Eliminar vuelo
- POST `/api/v1/flights/detect` - Detectar nuevo vuelo (Input I1)
- PUT `/api/v1/flights/{id}/status` - Actualizar estado

### Gates
- GET `/api/v1/gates` - Listar todos los gates
- GET `/api/v1/gates/{id}` - Obtener gate por ID
- POST `/api/v1/gates` - Crear nuevo gate
- PUT `/api/v1/gates/{id}` - Actualizar gate
- DELETE `/api/v1/gates/{id}` - Eliminar gate
- GET `/api/v1/gates/available` - Obtener gates disponibles
- PUT `/api/v1/gates/{id}/status` - Actualizar estado

### Asignaciones
- GET `/api/v1/assignments` - Listar asignaciones
- GET `/api/v1/assignments/{id}` - Obtener asignación por ID
- POST `/api/v1/assignments` - Crear asignación manual
- PUT `/api/v1/assignments/{id}` - Actualizar asignación
- DELETE `/api/v1/assignments/{id}` - Eliminar asignación
- GET `/api/v1/assignments/active` - Asignaciones activas

### Monitoreo
- GET `/api/v1/monitoring/health` - Estado del sistema
- GET `/api/v1/monitoring/statistics` - Estadísticas generales
- GET `/api/v1/monitoring/automata/state` - Estado del DFA

### Documentación
- GET `/swagger-ui.html` - Interfaz Swagger UI
- GET `/v3/api-docs` - Especificación OpenAPI JSON

---

## Configuración

### Variables de Entorno

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/skygate
spring.datasource.username=skygate_user
spring.datasource.password=secure_password

# JWT
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# MQTT (futuro)
mqtt.broker.url=tcp://localhost:1883
mqtt.client.id=skygate-backend
mqtt.username=mqtt_user
mqtt.password=mqtt_password

# WebSocket
websocket.allowed-origins=http://localhost:3000,http://localhost:5173
```

---

## Ejecución del Proyecto

### Desarrollo

```bash
# Clonar el repositorio
git clone https://github.com/usuario/skygate-backend.git

# Entrar al directorio
cd skygate-backend

# Compilar el proyecto
mvn clean install

# Ejecutar en modo desarrollo
mvn spring-boot:run
```

### Producción

```bash
# Compilar JAR
mvn clean package -DskipTests

# Ejecutar JAR
java -jar target/Backend-0.0.1-SNAPSHOT.jar
```

### Docker (futuro)

```bash
docker build -t skygate-backend .
docker run -p 8080:8080 skygate-backend
```

---

## Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con coverage
mvn test jacoco:report

# Tests de integración
mvn verify -P integration-tests
```

---

## Documentación Adicional

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **Actuator Health**: `http://localhost:8080/actuator/health`
- **H2 Console (dev)**: `http://localhost:8080/h2-console`

---

## Licencia

Proyecto académico- Universidad de Cartagena
Curso: Teoría de Automatas (TDA)
Autor: Julián Espitia, Mónica Vellojin
Semestre: 5/10 - Ingeniería de Software

---

## Contacto

Para consultas sobre el proyecto:
- Email: jmartineze@unicartagena.edu.co, mvellojinm@unicartagena.edu.co
- GitHub: @julianmeoficial
- LinkedIn: @julianmeoficial
