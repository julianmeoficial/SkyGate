# Optimización del Autómata SkyGate

## Información del Documento
- **Proyecto:** SkyGate - Sistema de Gestión Automatizada de Gates Aeroportuarios
- **Institución:** Universidad de Cartagena
- **Curso:** Teoría de Autómatas (TDA)
- **Fecha:** Noviembre 11, 2025
- **Versión:** 2.0

---

## 1. Análisis de Eficiencia

### 1.1 Complejidad Temporal

#### Operaciones Críticas

**Transición de Estado: O(1)**
- Búsqueda de estado actual: O(1) vía enum.
- Validación de input: O(1) vía switch.
- Actualización de estado: O(1) asignación.

**Búsqueda de Gate Disponible: O(n)**
- n = número de gates en el sistema.
- Filtrado por tipo: O(n).
- Validación de disponibilidad: O(1) por gate.

**Procesamiento de Cola de Espera: O(m)**
- m = número de vuelos en espera.
- Ordenamiento FIFO: O(1) para primer elemento.
- Búsqueda de gate compatible: O(n).

**Complejidad Total por Transición: O(n + m)**

**Evaluación: ACEPTABLE ✓**

Para sistemas con menos de 100 gates y 50 vuelos en espera simultáneamente, la performance es excelente.

### 1.2 Complejidad Espacial

**Estado del Autómata: O(1)**
- Estado por vuelo: constante.
- No se mantiene historial en memoria.

**Sistema de Espera: O(m)**
- m = vuelos en espera simultáneamente.
- Espacio proporcional a vuelos encolados.

**Cache de Gates: O(n)**
- n = número total de gates.

**Evaluación: EXCELENTE ✓**

Uso de memoria es lineal y predecible. No hay fugas de memoria identificadas.

---

## 2. Optimizaciones Propuestas

### 2.1 Indexación de Gates por Tipo

**Problema Actual:**
Búsqueda lineal O(n) en todos los gates para encontrar uno compatible.

**Solución:**
Implementar estructura de datos optimizada:

```java
Map<GateType, List<Gate>> gatesByType
```

**Implementación Sugerida:**

```java
@Service
public class GateIndexService {
    
    private Map<GateType, Set<Gate>> availableGatesByType;
    
    @PostConstruct
    public void initializeIndex() {
        availableGatesByType = new EnumMap<>(GateType.class);
        for (GateType type : GateType.values()) {
            availableGatesByType.put(type, new HashSet<>());
        }
        refreshIndex();
    }
    
    public Optional<Gate> findAvailableGate(GateType type) {
        Set<Gate> gates = availableGatesByType.get(type);
        return gates.stream()
            .filter(Gate::isAvailable)
            .findFirst();
    }
    
    public void markAsOccupied(Gate gate) {
        availableGatesByType.get(gate.getType()).remove(gate);
    }
    
    public void markAsAvailable(Gate gate) {
        availableGatesByType.get(gate.getType()).add(gate);
    }
}
```

**Beneficios:**
- ✓ Reducir búsqueda de O(n) a O(k), donde k = gates del tipo específico.
- ✓ Promedio k = n/3 para 3 tipos de gates.
- ✓ Mejora de performance: 3x más rápido.

**Complejidad:**
- Tiempo: O(k) donde k << n.
- Espacio: O(n) adicional para índices.

### 2.2 Cola de Prioridad para Espera

**Problema Actual:**
Cola FIFO simple no optimiza la utilización de recursos.

**Solución:**
Reemplazar cola simple con `PriorityQueue`:

```java
@Component
public class WaitingFlightPriorityQueue {
    
    private PriorityQueue<WaitingFlight> queue;
    
    @PostConstruct
    public void initialize() {
        queue = new PriorityQueue<>(
            Comparator.comparing(WaitingFlight::getPriority)
                      .thenComparing(WaitingFlight::getWaitTime)
        );
    }
    
    private int calculatePriority(Flight flight) {
        int priority = 0;
        
        // Factor 1: Retraso acumulado
        if (flight.getDelay() > 30) priority += 50;
        else if (flight.getDelay() > 15) priority += 25;
        
        // Factor 2: Tipo de vuelo
        if (flight.isInternational()) priority += 30;
        
        // Factor 3: Número de pasajeros
        priority += flight.getPassengerCount() / 10;
        
        // Factor 4: Conexiones críticas
        if (flight.hasConnections()) priority += 40;
        
        return priority;
    }
}
```

**Criterios de Prioridad:**
1. **Retraso acumulado** (peso: alto).
2. **Tipo de vuelo** - internacional vs doméstico (peso: medio).
3. **Número de pasajeros** (peso: bajo).
4. **Conexiones críticas** (peso: alto).

**Beneficios:**
- ✓ Optimizar utilización de gates.
- ✓ Reducir impacto de retrasos.
- ✓ Mejorar satisfacción de pasajeros.
- ✓ Priorizar vuelos con mayor impacto.

**Complejidad:**
- Inserción: O(log m).
- Extracción: O(log m).
- m = vuelos en espera.

### 2.3 Cache de Disponibilidad

**Problema Actual:**
Queries repetidas a base de datos para verificar disponibilidad.

**Solución:**
Mantener cache actualizado de gates disponibles:

```java
@Service
public class GateAvailabilityCacheService {
    
    private final Cache<GateType, Set<String>> availableGatesCache;
    private final int CACHE_EXPIRY_SECONDS = 30;
    
    @PostConstruct
    public void initializeCache() {
        availableGatesCache = Caffeine.newBuilder()
            .expireAfterWrite(CACHE_EXPIRY_SECONDS, TimeUnit.SECONDS)
            .maximumSize(10)
            .build();
    }
    
    public Set<String> getAvailableGates(GateType type) {
        return availableGatesCache.get(type, this::fetchFromDatabase);
    }
    
    @EventListener
    public void onGateOccupied(GateOccupiedEvent event) {
        invalidateCache(event.getGate().getType());
    }
    
    @EventListener
    public void onGateFreed(GateFreedEvent event) {
        invalidateCache(event.getGate().getType());
    }
    
    private void invalidateCache(GateType type) {
        availableGatesCache.invalidate(type);
    }
}
```

**Estrategia de Invalidación:**
- Invalidación basada en eventos.
- Time-to-live de 30 segundos como fallback.
- Invalidación selectiva por tipo de gate.

**Beneficios:**
- ✓ Evitar queries repetidas a base de datos.
- ✓ Reducir latencia de asignación de O(n) + DB query a O(1).
- ✓ Reducir carga en la base de datos.
- ✓ Mejorar tiempo de respuesta: 10-50x más rápido.

**Trade-offs:**
- Consistencia eventual (máximo 30 segundos).
- Memoria adicional: O(n).
- Complejidad de invalidación.

### 2.4 Procesamiento Asíncrono

**Problema Actual:**
Operaciones no críticas bloquean transiciones del autómata.

**Solución:**
Implementar procesamiento asíncrono:

```java
@Service
public class AsyncOperationsService {
    
    @Async("automataExecutor")
    public void updateDisplays(FlightAssignment assignment) {
        // Actualización de displays en aeropuerto
        displayService.updateFlightInfo(assignment);
    }
    
    @Async("automataExecutor")
    public void logTransition(StateTransition transition) {
        // Logging detallado de transiciones
        auditService.logTransition(transition);
    }
    
    @Async("automataExecutor")
    public void sendNotifications(FlightEvent event) {
        // Notificaciones push a usuarios
        notificationService.send(event);
    }
    
    @Async("automataExecutor")
    public void updateMetrics(AutomataMetrics metrics) {
        // Actualización de métricas de sistema
        metricsService.record(metrics);
    }
}

@Configuration
public class AsyncConfig {
    
    @Bean(name = "automataExecutor")
    public Executor automataExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("automata-async-");
        executor.initialize();
        return executor;
    }
}
```

**Operaciones Asíncronas:**
- ✓ Actualización de displays.
- ✓ Logging de transiciones.
- ✓ Notificaciones push.
- ✓ Actualización de métricas.

**Beneficios:**
- ✓ No bloquear transiciones críticas del autómata.
- ✓ Mejorar tiempo de respuesta percibido.
- ✓ Mejor utilización de recursos.
- ✓ Escalabilidad mejorada.

**Consideraciones:**
- Garantizar que fallos asíncronos no afecten el autómata.
- Implementar retry para operaciones críticas.
- Monitoreo de cola de tareas.

---

## 3. Análisis de Escalabilidad

### 3.1 Límites del Sistema Actual

**Capacidad Teórica:**
- **Gates:** 100-200 (límite práctico antes de degradación)
- **Vuelos Simultáneos:** 500-1000
- **Transiciones por Segundo:** 50-100

#### Cuellos de Botella Identificados

**1. Búsqueda de Gates Disponibles**
- **Problema:** Complejidad O(n) se vuelve problemática con n > 200.
- **Solución:** Indexación por tipo y cache.

**2. Procesamiento de Cola de Espera**
- **Problema:** Procesamiento secuencial de cola.
- **Solución:** Procesamiento paralelo con asignación múltiple.

**3. Conexiones WebSocket**
- **Problema:** Límite de conexiones concurrentes.
- **Solución:** Load balancing y clustering.

### 3.2 Estrategias de Escalamiento

#### Escalamiento Vertical (Scale Up)

**Descripción:**
- Aumentar recursos del servidor (CPU, RAM, I/O).

**Aplicabilidad:**
- Hasta 200 gates y 2000 vuelos/día.
- Solución simple y rápida.

**Limitaciones:**
- Costo incremental alto.
- Límite físico de hardware.
- Single point of failure.

#### Escalamiento Horizontal (Scale Out)

**Estrategia 1: Particionamiento**

```
┌─────────────────────────────────────┐
│      Load Balancer                  │
└──────────┬──────────────────────────┘
           │
     ┌─────┴─────┬─────────┐
     │           │         │
┌────▼────┐ ┌───▼────┐ ┌──▼─────┐
│Terminal │ │Terminal│ │Terminal│
│   A     │ │   B    │ │   C    │
│Instance │ │Instance│ │Instance│
└─────────┘ └────────┘ └────────┘
```

**Particionamiento por:**
- Terminales del aeropuerto.
- Aerolíneas.
- Tipo de vuelo (doméstico/internacional).

**Beneficios:**
- Aislamiento de fallos.
- Escalabilidad lineal.
- Menor complejidad de coordinación.

**Desafíos:**
- Coordinación inter-particiones.
- Reasignación entre terminales.
- Balanceo de carga.

**Estrategia 2: Clustering**

```
┌──────────────────────────────────────┐
│       Message Broker (RabbitMQ)      │
└────────┬─────────────────────────────┘
         │
    ┌────┴─────┬──────────┐
    │          │          │
┌───▼───┐  ┌──▼───┐  ┌───▼───┐
│Node 1 │  │Node 2│  │Node 3 │
└───┬───┘  └──┬───┘  └───┬───┘
    │         │          │
    └─────────┴──────────┘
             │
    ┌────────▼────────┐
    │  Shared Redis   │
    │  State Cache    │
    └─────────────────┘
```

**Componentes Requeridos:**
- Redis para estado compartido.
- RabbitMQ o Kafka para eventos.
- Database clustering para alta disponibilidad.
- Load balancer inteligente.

**Beneficios:**
- Alta disponibilidad.
- Distribución de carga.
- Tolerancia a fallos.

### 3.3 Proyección de Crecimiento

#### Escenario Pequeño (Aeropuerto Regional)
- **Gates:** 10-20.
- **Vuelos/día:** 50-100.
- **Evaluación:** EXCELENTE ✓✓✓
- **Acción:** Ninguna modificación requerida.

#### Escenario Medio (Aeropuerto Nacional)
- **Gates:** 50-80.
- **Vuelos/día:** 200-400.
- **Evaluación:** BUENO ✓✓
- **Acción:** Optimizaciones menores (cache, indexación).

#### Escenario Grande (Hub Internacional)
- **Gates:** 150-200.
- **Vuelos/día:** 800-1200.
- **Evaluación:** REQUIERE MEJORAS ⚠
- **Acción:** Indexación, cache y optimización de búsquedas.

#### Escenario Muy Grande (Mega Hub)
- **Gates:** 300+
- **Vuelos/día:** 2000+
- **Evaluación:** REQUIERE REINGENIERÍA ⚠⚠
- **Acción:** Clustering, particionamiento y arquitectura distribuida.

---

## 4. Mejores Prácticas de Optimización

### 4.1 Matriz de Transiciones Explícita

**Problema Actual:**
Validación de transiciones mediante múltiples `if/switch` dispersos.

**Solución:**
Crear matriz de transiciones explícita:

```java
@Configuration
public class TransitionMatrixConfig {
    
    private Map<StateInputPair, AutomataState> transitionMatrix;
    
    @PostConstruct
    public void initializeMatrix() {
        transitionMatrix = new HashMap<>();
        
        // S0 transitions
        transitionMatrix.put(
            new StateInputPair(S0, I1), S1
        );
        
        // S1 transitions
        transitionMatrix.put(
            new StateInputPair(S1, I2), S2
        );
        
        // S2 transitions
        transitionMatrix.put(
            new StateInputPair(S2, I3), S3
        );
        transitionMatrix.put(
            new StateInputPair(S2, I4), S6
        );
        
        // ... resto de transiciones
    }
    
    public Optional<AutomataState> getNextState(
        AutomataState current, 
        AutomataInput input
    ) {
        return Optional.ofNullable(
            transitionMatrix.get(new StateInputPair(current, input))
        );
    }
    
    public boolean isValidTransition(
        AutomataState current,
        AutomataInput input
    ) {
        return transitionMatrix.containsKey(
            new StateInputPair(current, input)
        );
    }
}

record StateInputPair(AutomataState state, AutomataInput input) {}
```

**Beneficios:**
- ✓ Validación O(1) constante.
- ✓ Configuración centralizada.
- ✓ Fácil mantenimiento y modificación.
- ✓ Testing simplificado.
- ✓ Visualización clara de todas las transiciones.


## 5. Métricas de Performance

### 5.1 Objetivos de Performance

**Latencia:**
- Tiempo de transición: < 100ms (percentil 95).
- Tiempo de asignación: < 500ms (percentil 95).
- Tiempo de respuesta API: < 200ms (percentil 95).

**Throughput:**
- Transiciones por segundo: > 100
- Asignaciones por minuto: > 200
- Requests concurrentes: > 500

**Recursos:**
- CPU: < 70% utilización promedio
- Memoria: < 2GB heap utilizado
- Conexiones DB: < 50 activas

### 5.2 Monitoreo y Métricas

**Métricas Clave:**

```java
@Component
public class AutomataMetrics {
    
    private final MeterRegistry registry;
    
    public void recordTransition(StateTransition transition) {
        // Timer para medir duración de transiciones
        Timer.builder("automata.transition.duration")
            .tag("from", transition.getFrom().name())
            .tag("to", transition.getTo().name())
            .register(registry)
            .record(transition.getDuration());
            
        // Counter para transiciones por estado
        Counter.builder("automata.transitions")
            .tag("state", transition.getTo().name())
            .register(registry)
            .increment();
    }
    
    public void recordQueueSize(int size) {
        Gauge.builder("automata.queue.size", () -> size)
            .register(registry);
    }
    
    public void recordGateUtilization(double utilization) {
        Gauge.builder("automata.gate.utilization", () -> utilization)
            .register(registry);
    }
}
```

---

## 6. Conclusión de Optimización

### Resumen de Optimizaciones

**Prioridad Alta:**
1. ✓ Matriz de transiciones explícita → Validación O(1).
2. ✓ Indexación de gates por tipo → Búsqueda 3x más rápida.
3. ✓ Cache de disponibilidad → Latencia 10-50x menor.

**Prioridad Media:**
4. ✓ Cola de prioridad → Mejor utilización de recursos.
5. ✓ Procesamiento asíncrono → Mejor throughput.

**Prioridad Baja:**
6. Event sourcing → Auditabilidad completa.
7. Clustering → Alta disponibilidad.

### Impacto Esperado

**Performance:**
- Reducción de latencia: 40-60%.
- Aumento de throughput: 2-3x.
- Mejor utilización de recursos: 30-50%.

**Escalabilidad:**
- Capacidad actual: 100 gates.
- Capacidad optimizada: 300+ gates.
- Throughput: 50 → 150+ transiciones/seg.

---

**Documento elaborado por:** Julián Espitia, Mónica Vellojín
**Clasificación:** Optimización de Performance.
