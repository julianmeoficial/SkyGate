# Análisis de Precisión del Autómata SkyGate

## Información del Documento
- **Proyecto:** SkyGate - Sistema de Gestión Automatizada de Gates Aeroportuarios
- **Institución:** Universidad de Cartagena
- **Curso:** Teoría de Autómatas (TDA)
- **Fecha:** Noviembre 11, 2025
- **Versión:** 2.0

---

## 1. Arquitectura del Autómata

### 1.1 Especificación Formal del DFA

**Quíntupla del Autómata:**
- **Q** = {S0, S1, S2, S3, S4, S5, S6, S7}
- **Σ** = {I1, I2, I3, I4, I5, I6}
- **δ**: Q × Σ → Q
- **q₀** = S0
- **F** = {S0, S7}

### 1.2 Definición de Estados

#### S0 - Sistema Inicializado
- Estado inicial y final del ciclo.
- Representa el sistema en espera o completado.
- **Transición:** I1 → S1

#### S1 - Aeronave Detectada
- Detección inicial de la aeronave entrante.
- Sistema en proceso de identificación.
- **Transición:** I2 → S2

#### S2 - Tipo Confirmado
- Tipo de aeronave identificado correctamente.
- Sistema preparado para buscar gate compatible.
- **Transiciones:** I3 → S3 o I4 → S6

#### S3 - Buscando Gate
- Búsqueda activa de gate disponible compatible.
- Estado intermedio de procesamiento.
- **Transición:** Automática → S4

#### S4 - Gate Asignado
- Gate compatible asignado exitosamente.
- Esperando arribo físico de la aeronave.
- **Transición:** I5 → S5

#### S5 - Aeronave Estacionada
- Aeronave física en el gate.
- Servicios de atención en progreso.
- **Transición:** I6 → S0

#### S6 - En Espera
- No hay gates disponibles.
- Vuelo encolado para asignación posterior.
- **Transición:** I3 (cuando se libera gate) → S3

#### S7 - Estado Final
- Ciclo completado, aeronave partió.
- Registros actualizados en base de datos.

### 1.3 Inputs del Sistema

#### I1 - Detección de Aeronave
- **Triggered:** Sistema externo o manual.
- **Valida:** Número de vuelo, origen, destino.
- **Crea:** Entidad Flight en estado DETECTED..

#### I2 - Confirmación de Tipo
- **Triggered:** Detección automática o manual.
- **Valida:** NARROW_BODY, WIDE_BODY, JUMBO.
- **Determina:** Compatibilidad con gates.

#### I3 - Gate Disponible
- **Triggered:** Sistema de asignación.
- **Valida:** Compatibilidad tipo aeronave.
- **Crea:** Entidad Assignment activa.

#### I4 - Sin Gates Disponibles
- **Triggered:** Sistema de asignación.
- **Acción:** Encolar vuelo en sistema de espera.
- **Notifica:** Actualización de pantallas.

#### I5 - Aeronave Arribo
- **Triggered:** Confirmación manual o sensores.
- **Actualiza:** Tiempo real de llegada.
- **Cambia:** Estado gate a OCCUPIED.

#### I6 - Aeronave Partió
- **Triggered:** Confirmación manual o sensores.
- **Libera:** Gate para reasignación.
- **Marca:** Assignment como inactivo.

### 1.4 Outputs del Sistema

#### O1 - Activar LEDs
- **Estado:** Verde para gate asignado.
- **Medio:** Control hardware vía MQTT.
- **Futuro:** Integración con Arduino/ESP32.

#### O2 - Notificación de Asignación
- **Medio:** WebSocket push notification.
- **Destino:** Frontend en tiempo real.
- **Contenido:** Detalles de asignación.

#### O3 - LEDs Ocupado
- **Estado:** Rojo para gate ocupado.
- **Medio:** Control hardware vía MQTT.
- **Indicador:** Gate no disponible.

#### O4 - Actualizar Pantalla
- **Medio:** Sistema de displays.
- **Contenido:** Lista de vuelos en espera.
- **Actualización:** Tiempo real.

#### O5 - Actualizar Base de Datos
- **Medio:** JPA/Hibernate.
- **Contenido:** Estados, timestamps, relaciones.
- **Persistencia:** PostgreSQL (producción).

---

## 2. Análisis de Precisión

### 2.1 Determinismo

**Evaluación: EXCELENTE ✓**

El autómata implementado es completamente determinístico:

- ✓ Cada estado tiene transiciones únicas para cada input.
- ✓ No existen ambigüedades en las transiciones.
- ✓ Función de transición δ está claramente definida.
- ✓ No hay estados de aceptación múltiples para la misma secuencia.

**Evidencia:**
- Cada input produce exactamente una transición.
- Estado siguiente está determinado únicamente por estado actual e input.
- No se requiere backtracking ni exploración de caminos.

### 2.2 Completitud

**Evaluación: BUENA ✓**

#### Casos Cubiertos
- ✓ Flujo nominal: Detección → Asignación → Arribo → Partida.
- ✓ Caso de excepción: Sin gates disponibles con sistema de espera.
- ✓ Reasignación automática cuando se libera un gate.

#### Casos No Cubiertos Explícitamente
- ⚠ Cancelación de vuelo después de asignación.
- ⚠ Cambio de gate por emergencia.
- ⚠ Mantenimiento de gate durante operación.
- ⚠ Retraso indefinido con timeout.
- ⚠ Falla de hardware o comunicación.

**Recomendación:** Agregar estados y transiciones para manejar excepciones críticas.

### 2.3 Validación de Transiciones

**Evaluación: BUENA ✓**

#### Controles Implementados
- ✓ Validación de estado origen antes de transición.
- ✓ Validación de input permitido para estado actual.
- ✓ Excepciones personalizadas: `InvalidStateTransitionException`.
- ✓ Logging detallado de transiciones inválidas.

#### Mejoras Sugeridas
- Implementar matriz de transiciones explícita para validación O(1).
- Agregar validación de secuencias de inputs inválidas.
- Implementar rollback automático en caso de falla.

### 2.4 Sincronización con Estado del Dominio

**Evaluación: EXCELENTE ✓**

El autómata mantiene sincronización perfecta con el modelo de dominio:

#### Sincronización Implementada
- ✓ Cada transición actualiza entidades correspondientes (Flight, Gate, Assignment).
- ✓ Estados del autómata mapean directamente a `FlightStatus` enum.
- ✓ Transacciones atómicas garantizan consistencia.
- ✓ Eventos de dominio (`GateFreedEvent`) desacoplan lógica.

#### Fortalezas
- Uso de Spring Events para comunicación desacoplada.
- Transacciones JPA garantizan atomicidad.
- WebSocket notifica cambios en tiempo real.

---

## 3. Análisis de Robustez

### 3.1 Manejo de Errores

**Evaluación: BUENA ✓**

#### Excepciones Definidas
- `InvalidStateTransitionException`
- `GateAlreadyOccupiedException`
- `NoAvailableGateException`
- `FlightNotFoundException`
- `GateNotFoundException`

#### Fortalezas
- ✓ Excepciones específicas por tipo de error.
- ✓ `GlobalExceptionHandler` centralizado.
- ✓ Respuestas HTTP consistentes.
- ✓ Logging completo de errores.

#### Mejoras Sugeridas
- Agregar estrategia de retry para fallos transitorios.
- Implementar circuit breaker para servicios externos.
- Agregar timeouts configurables.
- Implementar compensación de transacciones.

### 3.2 Tolerancia a Fallos

**Evaluación: ACEPTABLE ⚠**

#### Mecanismos Implementados
- ✓ Transacciones atómicas con JPA.
- ✓ Rollback automático en excepciones.
- ✓ Validación de estado antes de transiciones.

#### Vulnerabilidades Identificadas

**1. Falla de Comunicación WebSocket**
- **Impacto:** Frontend no recibe actualizaciones.
- **Mitigación Actual:** Reconexión automática.
- **Mejora Sugerida:** Queue de mensajes no entregados.

**2. Falla de Base de Datos**
- **Impacto:** Sistema completo no funcional.
- **Mitigación Actual:** Ninguna.
- **Mejora Sugerida:** Modo de operación degradado con cache.

**3. Inconsistencia Estado-Datos**
- **Impacto:** Autómata desincronizado con entidades.
- **Mitigación Actual:** Transacciones atómicas.
- **Mejora Sugerida:** Verificación periódica de consistencia.

**4. Deadlock en Asignación Concurrente**
- **Impacto:** Asignación múltiple del mismo gate.
- **Mitigación Actual:** Locks de JPA.
- **Mejora Sugerida:** Optimistic locking con version.

### 3.3 Concurrencia

**Evaluación: BUENA ✓**

#### Mecanismos de Control
- ✓ Locks pesimistas en operaciones críticas.
- ✓ Transacciones con nivel de aislamiento apropiado.
- ✓ Sincronización en procesamiento de cola.

#### Escenarios Críticos

**1. Asignación Concurrente**
- **Problema:** Dos vuelos intentan asignar mismo gate.
- **Solución Actual:** Lock a nivel de base de datos.
- **Evaluación:** Efectivo pero puede causar contention.

**2. Liberación y Reasignación**
- **Problema:** Gate liberado mientras otro vuelo lo solicita.
- **Solución Actual:** Eventos asíncronos.
- **Evaluación:** Race condition posible.

---

## 4. Documentación y Testing

### 4.1 Estado Actual de Documentación

**Evaluación: EXCELENTE ✓**

#### Documentación Existente
- ✓ Documentación completa de backend y frontend.
- ✓ Descripción detallada de estados, inputs y outputs.
- ✓ Arquitectura bien documentada.
- ✓ Endpoints y servicios documentados.

#### Mejoras Sugeridas
- Diagrama de estados formal (notación matemática).
- Ejemplos de secuencias válidas e inválidas.
- Casos de uso documentados paso a paso.
- Decision tree para resolución de problemas.

### 4.2 Estrategia de Testing

#### Testing Unitario
Tests para cada componente del autómata:
- `AutomataService`
- `StateTransitionService`
- `AutomataStateManager`
- `WaitingFlightProcessor`

**Casos de Test Requeridos:**
- Transiciones válidas para cada estado.
- Inputs inválidos para cada estado.
- Condiciones de borde.
- Manejo de excepciones.

#### Testing de Integración
Escenarios end-to-end:
- Flujo completo de vuelo exitoso.
- Vuelo con espera y reasignación.
- Múltiples vuelos concurrentes.
- Fallas de componentes.

#### Testing de Performance
Pruebas de carga:
- 100 transiciones concurrentes.
- 1000 vuelos en sistema.
- 200 gates activos.
- Stress testing hasta falla.

#### Testing de Concurrencia
Escenarios de race conditions:
- Asignación simultánea de mismo gate.
- Liberación y reasignación concurrente.
- Actualizaciones concurrentes de estado.

### 4.3 Cobertura de Tests

**Objetivo Mínimo: 80%**
- Líneas de código cubiertas.
- Branches cubiertas.
- Condiciones cubiertas.

**Tests Críticos:**
- 100% cobertura en lógica de transiciones.
- 100% cobertura en validaciones.
- 100% cobertura en manejo de excepciones.

---

## 5. Conclusión del Análisis de Precisión

### Evaluación General

El autómata implementado en SkyGate demuestra un diseño formal correcto y una implementación técnica robusta. Es completamente determinístico y mantiene sincronización perfecta con el dominio.

### Fortalezas Principales
- ✓ Diseño formal correcto del DFA.
- ✓ Implementación limpia y mantenible.
- ✓ Sincronización excelente con modelo de dominio.
- ✓ Comunicación en tiempo real efectiva.
- ✓ Arquitectura bien organizada.

### Áreas de Mejora Identificadas
- ⚠ Manejo de casos excepcionales.
- ⚠ Tolerancia a fallos avanzada.
- ⚠ Validación de secuencias de inputs.
- ⚠ Estrategias de recuperación ante fallos.

### Métricas de Precisión

**Objetivos:**
- **Precisión de Transiciones:** 100% de transiciones válidas ejecutadas correctamente.
- **Rechazo de Inválidos:** 0% de transiciones inválidas aceptadas.
- **Consistencia:** 0% de inconsistencias estado-datos.
- **Confiabilidad:** Uptime > 99.9%.

---

**Documento elaborado por:** Julián Espitia, Mónica Vellojín
**Clasificación:** Análisis de Precisión