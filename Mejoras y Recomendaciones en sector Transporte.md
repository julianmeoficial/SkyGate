# Mejoras y Recomendaciones Finales del Autómata SkyGate
## Aplicación en el Sector Transporte Aeroportuario

## Información del Documento
- **Proyecto:** SkyGate - Sistema de Gestión Automatizada de Gates Aeroportuarios
- **Institución:** Universidad de Cartagena
- **Curso:** Teoría de Autómatas (TDA)
- **Fecha:** Noviembre 11, 2025
- **Versión:** 3.0

---

## 1. Introducción

El presente documento sintetiza las mejoras prioritarias identificadas para el autómata SkyGate y proporciona recomendaciones específicas para su implementación en el sector transporte aeroportuario. Las propuestas se organizan en tres fases temporales y consideran tanto aspectos técnicos como operacionales propios de la industria aeronáutica.

---

## 2. Mejoras Prioritarias

### 2.1 Fase 1: Fundamentos (1-3 meses)

#### Implementación de Matriz de Transiciones Explícita

La primera mejora prioritaria consiste en reemplazar la lógica dispersa de validación de transiciones por una matriz explícita y centralizada. Actualmente, las validaciones se encuentran distribuidas en múltiples métodos y clases, lo cual dificulta el mantenimiento y aumenta el riesgo de inconsistencias.

Una matriz de transiciones explícita permitiría validar cualquier transición en tiempo constante, simplificaría la configuración del autómata y facilitaría la visualización completa del comportamiento del sistema. Esta estructura también mejoraría significativamente la testabilidad del código, permitiendo verificar exhaustivamente todas las transiciones posibles e imposibles del sistema.

La implementación de esta mejora representaría aproximadamente dos a tres días de trabajo y tendría un impacto alto en la calidad y mantenibilidad del código.

#### Incorporación de Estados de Excepción

El sistema actual maneja eficientemente el flujo nominal de operaciones, pero carece de estados específicos para situaciones excepcionales que son comunes en operaciones aeroportuarias reales. Se propone la incorporación de tres nuevos estados:

**Estado de Cancelación**: Este estado manejaría vuelos cancelados después de haber sido detectados o incluso después de tener un gate asignado. En operaciones aeroportuarias reales, las cancelaciones pueden ocurrir en cualquier momento debido a condiciones meteorológicas adversas, problemas mecánicos de la aeronave o decisiones comerciales de las aerolíneas. El autómata debería poder transicionar a este estado desde múltiples puntos del flujo normal, liberando recursos asignados y notificando a todos los sistemas integrados.

**Estado de Mantenimiento**: Durante las operaciones normales, pueden surgir necesidades imprevistas de mantenimiento en un gate que ya tiene una aeronave asignada o incluso estacionada. Este estado permitiría al sistema manejar estas situaciones de emergencia, reasignando el vuelo a otro gate disponible si es necesario y marcando el gate problemático como no disponible temporalmente.

**Estado de Reasignación de Emergencia**: Situaciones operacionales como emergencias médicas, amenazas de seguridad o necesidades de aislamiento de aeronaves requieren cambios inmediatos de gate. Este estado facilitaría estas transiciones críticas manteniendo la integridad del sistema y la trazabilidad de las acciones tomadas.

La implementación de estos estados requeriría aproximadamente tres a cinco días de trabajo, pero proporcionaría al sistema la capacidad de manejar situaciones reales que actualmente quedarían fuera de su alcance operacional.

#### Sistema de Cache para Disponibilidad de Gates

La búsqueda repetida de gates disponibles en la base de datos representa uno de los principales cuellos de botella del sistema actual. Cada vez que un vuelo necesita asignación, el sistema debe consultar la base de datos para identificar gates compatibles y disponibles, lo cual introduce latencia y aumenta la carga en el servidor de base de datos.

Un sistema de cache inteligente mantendría en memoria una estructura actualizada de gates disponibles, organizada por tipo de aeronave. Este cache se invalidaría automáticamente cada vez que cambie el estado de un gate, asegurando consistencia mientras proporciona tiempos de respuesta significativamente menores.

Esta optimización podría reducir la latencia de asignación entre diez y cincuenta veces, dependiendo de la configuración del sistema de base de datos. Adicionalmente, reduciría la carga en la base de datos en aproximadamente setenta a ochenta por ciento, permitiendo que el sistema escale a mayor número de operaciones concurrentes.


#### Suite Completa de Pruebas

La calidad de cualquier sistema crítico se fundamenta en una estrategia robusta de pruebas. Actualmente, el proyecto carece de cobertura de pruebas exhaustiva, lo cual representa un riesgo significativo para operaciones en producción.

Se propone desarrollar una suite completa que incluya pruebas unitarias para cada componente del autómata, pruebas de integración para verificar el comportamiento end-to-end del sistema, y pruebas de concurrencia para validar el comportamiento bajo condiciones de alta carga. Las pruebas unitarias deberían verificar cada transición válida desde cada estado, confirmar el rechazo apropiado de transiciones inválidas, y validar el manejo correcto de todas las excepciones. Las pruebas de integración deberían simular flujos completos de vuelos, desde la detección hasta la partida, incluyendo casos con espera y reasignación.

El objetivo mínimo de cobertura debería ser ochenta por ciento para líneas de código, con cien por ciento de cobertura obligatoria para la lógica de transiciones, validaciones y manejo de excepciones. El esfuerzo estimado es de cinco a siete días de trabajo, con un impacto alto en la confiabilidad del sistema.

---

### 2.2 Fase 2: Optimización (3-6 meses)

#### Sistema Integral de Métricas

Un sistema de gestión aeroportuaria debe proporcionar visibilidad completa de sus operaciones en tiempo real. Se propone implementar un sistema integral de métricas que capture y analice indicadores clave de rendimiento específicos del sector aeroportuario.

Este sistema debería medir continuamente la utilización de gates por tipo y por terminal, el tiempo promedio de permanencia de aeronaves en los gates, la cantidad de vuelos en espera y su tiempo de espera, la eficiencia del proceso de asignación, y los tiempos de respuesta del autómata. Las métricas deberían calcularse en tiempo real y presentarse en un dashboard accesible para operadores y supervisores. Adicionalmente, el sistema debería mantener históricos para análisis de tendencias y generación de reportes operacionales.

Indicadores como el porcentaje de vuelos puntuales, la tasa de cambios de gate, el tiempo promedio de turnaround por tipo de aeronave, y la utilización peak de capacidad son fundamentales para evaluar el desempeño operacional del aeropuerto.


#### Optimistic Locking para Reducción de Contención

El sistema actual utiliza locks pesimistas para prevenir asignaciones conflictivas del mismo gate a múltiples vuelos. Si bien esta estrategia es efectiva, puede causar contención significativa bajo alta concurrencia, reduciendo el throughput del sistema.

La implementación de optimistic locking, basado en versionado de entidades, permitiría que múltiples transacciones lean y procesen información simultáneamente, detectando conflictos solo al momento de confirmar cambios. En caso de conflicto, el sistema reintentatía la operación con información actualizada.Este cambio reduciría la contención en operaciones concurrentes entre sesenta y setenta por ciento, mejorando el throughput entre cincuenta y cien por ciento bajo carga alta. El sistema mantendría la misma garantía de consistencia mientras escala mejor horizontalmente.

#### Event Sourcing para Auditoría Completa

Las operaciones aeroportuarias están sujetas a estrictos requisitos regulatorios que exigen trazabilidad completa de todas las acciones tomadas en el sistema. Event sourcing proporciona esta capacidad almacenando todos los cambios de estado como una secuencia inmutable de eventos. En lugar de mantener únicamente el estado actual de cada vuelo y gate, el sistema mantendría un registro completo de todos los eventos que llevaron a ese estado. Esto permitiría reconstruir el estado del sistema en cualquier punto del tiempo, facilitar auditorías regulatorias, depurar problemas complejos mediante replay de eventos, y analizar patrones operacionales históricos.

Event sourcing también facilita la implementación de funcionalidades avanzadas como deshacer acciones, analizar el impacto hipotético de decisiones alternativas, y generar reportes detallados para cumplimiento regulatorio.

#### Sistema de Timeouts Configurables

En operaciones reales, es necesario establecer límites temporales para diferentes estados del autómata. Un vuelo que tiene un gate asignado pero cuya aeronave no arriba en el tiempo esperado debería liberar ese recurso. Una aeronave que excede el tiempo máximo de turnaround debería generar alertas para intervención operacional. Se propone implementar un sistema de timeouts configurables que monitoree continuamente todos los vuelos en el sistema y genere acciones automáticas o alertas cuando se excedan umbrales temporales definidos.

Los timeouts deberían ser configurables según el tipo de operación y el contexto operacional. Por ejemplo, el timeout para arribo después de asignación de gate podría ser treinta minutos, mientras que el tiempo máximo de turnaround podría variar entre treinta minutos para aeronaves regionales y dos horas para aeronaves wide-body en vuelos internacionales. El sistema de timeouts mejoraría significativamente la eficiencia operacional al prevenir que recursos valiosos queden bloqueados indefinidamente por situaciones anómalas.

---

### 2.3 Fase 3: Escalabilidad y Funcionalidades Avanzadas (6-12 meses)

#### Clustering para Alta Disponibilidad

Aeropuertos de mediano y gran tamaño requieren sistemas con disponibilidad superior al noventa y nueve punto nueve por ciento. La arquitectura actual, basada en una única instancia de la aplicación, representa un único punto de falla que es inaceptable para operaciones críticas. Se propone evolucionar la arquitectura hacia un sistema distribuido con múltiples instancias de la aplicación balanceadas por carga. Esta arquitectura incluiría un cluster de Redis para compartir estado entre instancias, un broker de mensajes como RabbitMQ para coordinación de eventos, y un cluster de base de datos PostgreSQL con replicación para alta disponibilidad de datos.

Esta arquitectura proporcionaría tolerancia a fallos de nodos individuales, permitiría realizar actualizaciones sin tiempo de inactividad, y facilitaría el escalamiento horizontal agregando más instancias según la carga operacional.

#### Optimización mediante Machine Learning

El comportamiento histórico de operaciones aeroportuarias contiene patrones valiosos que pueden utilizarse para optimizar la asignación de gates y predecir problemas operacionales. La incorporación de capacidades de machine learning permitiría al sistema aprender de experiencias pasadas y tomar decisiones más inteligentes. Modelos predictivos podrían estimar con alta precisión el tiempo de turnaround esperado para cada vuelo basándose en factores como tipo de aeronave, aerolínea, hora del día, condiciones meteorológicas y carga de pasajeros. Esta información permitiría asignar gates de manera más eficiente, anticipando cuándo estarán disponibles para reasignación.

Algoritmos de optimización podrían considerar múltiples vuelos simultáneamente, encontrando configuraciones de asignación que minimicen tiempos de espera totales, reduzcan cambios de gate, y maximicen la utilización de recursos. La implementación de capacidades de machine learning requeriría sesenta a noventa días de trabajo, incluyendo recolección y preparación de datos históricos, entrenamiento de modelos, y integración con el sistema existente. El impacto sería muy alto, potencialmente reduciendo demoras entre quince y veinticinco por ciento y mejorando significativamente la experiencia de pasajeros y aerolíneas.

#### Simulador del Autómata

Antes de implementar cambios significativos en un sistema operacional crítico, es fundamental poder validar su comportamiento en un ambiente controlado. Un simulador del autómata permitiría probar configuraciones, validar comportamiento bajo diferentes escenarios de carga, y entrenar personal operacional sin riesgo para operaciones reales. El simulador debería poder generar escenarios realistas de tráfico aéreo, simular el comportamiento completo del autómata bajo esas condiciones, y generar métricas detalladas sobre el desempeño del sistema. Esto facilitaría capacity planning, permitiendo determinar cuántos gates se necesitan para manejar volúmenes proyectados de tráfico.

También serviría como herramienta de entrenamiento, permitiendo que operadores y supervisores practiquen el manejo de situaciones normales y excepcionales en un ambiente seguro antes de enfrentarlas en operaciones reales.

---

## 3. Recomendaciones para el Sector Transporte Aeroportuario

### 3.1 Integración con Ecosistema Aeroportuario

#### Sistemas AODB (Airport Operational Database)

Un sistema de gestión de gates no opera en aislamiento. Los aeropuertos modernos utilizan sistemas AODB que centralizan toda la información operacional del aeropuerto. SkyGate debe integrarse bidireccionalemente con estos sistemas, recibiendo actualizaciones sobre cambios en horarios de vuelos, cancelaciones y demoras, mientras proporciona información actualizada sobre asignaciones de gates y estados de vuelos.

Esta integración es fundamental para mantener consistencia de información en todos los sistemas del aeropuerto y evitar discrepancias que puedan causar confusión operacional.

#### Sistemas FIDS (Flight Information Display System)

Los displays de información de vuelos distribuidos por todo el aeropuerto deben reflejar información actualizada sobre gates asignados. SkyGate debe integrarse con los sistemas FIDS para asegurar que pasajeros y personal operacional tengan acceso a información precisa y actualizada en tiempo real.

La integración puede realizarse mediante protocolos estándar como MQTT, permitiendo que el sistema publique actualizaciones que son consumidas por los displays de manera eficiente y escalable.

#### Sistemas BHS (Baggage Handling System)

La asignación de un gate determina también hacia dónde debe direccionarse el equipaje de los pasajeros. Los sistemas de manejo de equipaje necesitan conocer las asignaciones de gates con suficiente anticipación para configurar correctamente las rutas de transporte de maletas.

SkyGate debe notificar al sistema BHS cada vez que se realiza una asignación, proporcionando información sobre el gate, el carrusel de equipaje asociado y los tiempos estimados. Esto asegura que el equipaje llegue al lugar correcto en el momento apropiado, mejorando la experiencia de los pasajeros.

### 3.2 Cumplimiento Regulatorio

#### Estándares IATA

La International Air Transport Association establece estándares y mejores prácticas para operaciones aeroportuarias. SkyGate debe implementar funcionalidades que faciliten el cumplimiento de estos estándares, particularmente en lo relacionado con tiempos de turnaround y métricas de puntualidad.

El sistema debe poder generar reportes que demuestren cumplimiento con estándares IATA, incluyendo estadísticas de On-Time Performance, tiempos promedio de turnaround por tipo de aeronave, y eficiencia en la utilización de recursos.

#### Requisitos de Auditoría

Autoridades aeronáuticas nacionales e internacionales requieren que los aeropuertos mantengan registros detallados de todas las operaciones. Event sourcing y un sistema robusto de auditoría permiten cumplir estos requisitos, proporcionando trazabilidad completa de cada acción tomada en el sistema.

Los registros de auditoría deben incluir información sobre quién realizó cada acción, cuándo se realizó, desde qué sistema o ubicación, y qué cambios se efectuaron. Esta información es crucial tanto para auditorías regulatorias como para investigaciones de incidentes.

### 3.3 Seguridad y Control de Acceso

Las operaciones aeroportuarias son sensibles desde perspectivas tanto de seguridad como de safety. El sistema debe implementar controles de acceso granulares que limiten qué usuarios pueden realizar qué acciones.

Operadores regulares deberían poder realizar transiciones normales del autómata pero requerir aprobación de supervisores para acciones excepcionales como cancelaciones, reasignaciones de emergencia o cambios de configuración. Los logs de seguridad deben registrar todos los intentos de acceso, exitosos o fallidos, para detectar posibles intentos de acceso no autorizado.

La autenticación debe integrarse con los sistemas de gestión de identidad corporativos del aeropuerto, típicamente basados en Active Directory o LDAP, para mantener consistencia en la administración de usuarios y simplificar el proceso de onboarding y offboarding de personal.

### 3.4 Planificación de Capacidad

Uno de los desafíos más importantes en gestión aeroportuaria es determinar cuántos gates son necesarios para manejar eficientemente el volumen proyectado de tráfico. Muy pocos gates causan congestión y demoras, mientras que demasiados gates representan inversión de capital sub-utilizada.

El simulador propuesto permitiría realizar estudios de capacity planning, simulando diferentes escenarios de tráfico futuro y determinando el número óptimo de gates por tipo. Estos análisis pueden informar decisiones de inversión en infraestructura, típicamente involucrando decenas o cientos de millones de dólares.

### 3.5 Gestión de Picos Operacionales

El tráfico aeroportuario no es uniforme a lo largo del día. La mayoría de aeropuertos experimentan picos de actividad en horas específicas, cuando múltiples vuelos arriban o parten simultáneamente. El sistema debe ser capaz de manejar estos picos sin degradación significativa de performance.

Las optimizaciones propuestas, particularmente el cache de disponibilidad y el optimistic locking, son fundamentales para mantener tiempos de respuesta aceptables durante periodos de alta demanda. El sistema debe poder procesar al menos cien transiciones por segundo para manejar picos operacionales en aeropuertos medianos.

### 3.6 Disaster Recovery y Business Continuity

Interrupciones en el sistema de gestión de gates pueden paralizar completamente las operaciones de un aeropuerto, causando demoras masivas, cancelaciones y pérdidas económicas significativas. Es fundamental implementar planes robustos de disaster recovery y business continuity.

El sistema debe realizar backups automáticos frecuentes del estado completo, permitiendo restauración rápida en caso de fallos catastróficos. Adicionalmente, debe incluir capacidad de operar en modo degradado cuando ciertos componentes no están disponibles, priorizando funcionalidad crítica sobre funcionalidad complementaria.

Los procedimientos de recuperación deben estar documentados exhaustivamente y el personal debe estar entrenado en su ejecución. Simulacros periódicos de disaster recovery aseguran que los procedimientos funcionan correctamente y que el personal está preparado para ejecutarlos bajo presión.

### 3.7 Consideraciones de Ciberseguridad

Los sistemas aeroportuarios son infraestructura crítica y objetivos potenciales de ciberataques. SkyGate debe implementar múltiples capas de seguridad para proteger contra amenazas externas e internas.

Esto incluye encriptación de datos en tránsito y en reposo, autenticación multi-factor para usuarios con privilegios elevados, segmentación de red para aislar sistemas críticos, monitoreo continuo de seguridad para detectar comportamiento anómalo, y procedimientos de respuesta a incidentes claramente definidos. Las vulnerabilidades de seguridad deben ser evaluadas regularmente mediante auditorías de seguridad y pruebas de penetración realizadas por especialistas independientes. Las actualizaciones de seguridad deben aplicarse con prioridad, siguiendo un proceso que minimice interrupciones operacionales.

---

## 4. Aplicabilidad por Tamaño de Aeropuerto

### 4.1 Aeropuertos Regionales (10-20 gates, 50-100 vuelos/día)

Para aeropuertos pequeños, la implementación actual de SkyGate es más que suficiente. El sistema proporciona todas las funcionalidades necesarias para gestionar eficientemente las operaciones con excelente performance y confiabilidad.

Se recomienda implementar únicamente las mejoras de Fase 1, particularmente la matriz de transiciones explícita y los estados de excepción, para proporcionar mayor robustez operacional. Las optimizaciones de performance de Fase 2 y 3 no son necesarias para este volumen de operaciones. La inversión en SkyGate para un aeropuerto regional representaría una modernización significativa de sus capacidades operacionales, proporcionando un sistema profesional a una fracción del costo de soluciones comerciales establecidas.

### 4.2 Aeropuertos Nacionales (50-80 gates, 200-400 vuelos/día)

Aeropuertos de tamaño medio se beneficiarían significativamente de las optimizaciones de Fase 2. El sistema de métricas avanzado proporcionaría visibilidad operacional que es crítica para gestionar eficientemente un volumen considerable de operaciones.

El cache de disponibilidad y el optimistic locking mejorarían el performance durante picos operacionales, asegurando que el sistema responda adecuadamente incluso cuando múltiples vuelos requieren asignación simultánea. Event sourcing proporcionaría las capacidades de auditoría que son cada vez más requeridas por autoridades regulatorias nacionales e internacionales. La inversión en estas mejoras se justificaría plenamente por la mejora en eficiencia operacional y capacidad de cumplimiento regulatorio.

### 4.3 Hubs Internacionales (150-200 gates, 800-1200 vuelos/día)

Para aeropuertos grandes que funcionan como hubs internacionales, todas las mejoras propuestas son altamente recomendables. El volumen y complejidad de operaciones requieren un sistema extremadamente robusto, eficiente y escalable.

La arquitectura de clustering es esencial para proporcionar la alta disponibilidad que operaciones de esta escala requieren. El sistema debe poder continuar operando incluso si uno o más nodos fallan, sin interrupción perceptible del servicio. Machine learning proporcionaría ventajas competitivas significativas, optimizando la utilización de gates y reduciendo demoras de manera que tendría impacto medible en métricas operacionales y satisfacción de pasajeros y aerolíneas.

El simulador sería una herramienta invaluable para planificación de capacidad y para evaluar el impacto de cambios en configuración o procedimientos operacionales antes de implementarlos en producción.

### 4.4 Mega-Hubs (300+ gates, 2000+ vuelos/día)

Aeropuertos del tamaño de mega-hubs como Atlanta, Beijing o Dubai requieren no solo todas las mejoras propuestas sino potencialmente reingeniería adicional para soportar la escala masiva de operaciones. Consideraciones adicionales incluirían particionamiento del sistema por terminales o áreas del aeropuerto, cada una con su propia instancia del autómata coordinándose a través de una capa de orquestación superior. Esto permitiría escalar casi linealmente agregando más particiones según crece el aeropuerto.

La inversión en capacidades avanzadas de inteligencia artificial sería altamente justificada, dado el impacto económico que pequeñas mejoras en eficiencia tienen a esta escala. Una reducción de cinco por ciento en tiempos de espera o demoras podría traducirse en ahorros de millones de dólares anuales.

---

## 5. Retorno de Inversión

### 5.1 Costos de Implementación

Los costos de implementación de SkyGate y sus mejoras propuestas son significativamente menores que soluciones comerciales establecidas. El desarrollo de Fase 1 requeriría aproximadamente tres meses de trabajo de un equipo pequeño de desarrollo, Fase 2 otros tres meses, y Fase 3 seis meses adicionales.

Comparado con sistemas comerciales que pueden costar varios millones de dólares en licencias, implementación y mantenimiento anual, SkyGate representa una alternativa económicamente viable particularmente para aeropuertos pequeños y medianos.

### 5.2 Beneficios Operacionales

Los beneficios de un sistema automatizado de gestión de gates incluyen reducción de errores humanos en asignación, optimización de utilización de gates, reducción de tiempos de espera para vuelos, mejora en métricas de puntualidad, y reducción de carga de trabajo para personal operacional.

Estudios de caso en aeropuertos que han implementado sistemas similares reportan mejoras en utilización de gates entre diez y veinte por ciento, reducción de demoras atribuibles a asignación de gates en aproximadamente quince por ciento, y mejoras en satisfacción de aerolíneas y pasajeros.

### 5.3 Escalabilidad de Costos

Una ventaja significativa de SkyGate es que los costos escalan mucho más favorablemente que soluciones comerciales. Mientras que proveedores comerciales típicamente cobran por número de gates o volumen de transacciones, SkyGate puede escalar agregando hardware de servidor de commodities relativamente económico.

Las mejoras de software propuestas son inversiones únicas que benefician permanentemente al sistema sin costos recurrentes de licenciamiento. Esto hace que el costo total de propiedad a largo plazo sea significativamente menor.

---

## 6. Riesgos y Mitigaciones

### 6.1 Riesgos de Implementación

La implementación de un sistema crítico como SkyGate conlleva riesgos que deben ser cuidadosamente gestionados. El riesgo principal es interrumpir operaciones existentes durante la transición.

Este riesgo se mitiga mediante implementación en fases, comenzando con modo sombra donde el sistema opera en paralelo con procedimientos existentes sin controlar operaciones reales. Una vez validado exhaustivamente, se puede realizar transición gradual de operaciones al nuevo sistema.

### 6.2 Riesgos Operacionales

Fallas del sistema durante operaciones pueden causar impactos severos. Este riesgo se mitiga mediante las mejoras de alta disponibilidad propuestas en Fase 3, particularmente clustering y capacidad de operar en modo degradado.

Adicionalmente, procedimientos de fallback claros deben estar documentados y el personal debe estar entrenado para operar manualmente en caso de falla completa del sistema, asegurando que operaciones puedan continuar aunque sea con eficiencia reducida.

### 6.3 Riesgos de Seguridad

Como sistema que controla infraestructura crítica, SkyGate es un objetivo potencial de ciberataques. Los riesgos de seguridad se mitigan mediante implementación de mejores prácticas de ciberseguridad, auditorías de seguridad regulares, y monitoreo continuo de amenazas.

El sistema debe ser diseñado asumiendo que eventualmente será comprometido, implementando controles que limiten el daño potencial de una brecha de seguridad exitosa mediante segmentación, principio de mínimo privilegio y detección temprana de comportamiento anómalo.

---

## 7. Conclusiones

### 7.1 Viabilidad Técnica

El análisis demuestra que SkyGate representa una implementación técnicamente sólida de un autómata finito determinista aplicado a gestión aeroportuaria. Las mejoras propuestas son técnicamente factibles y seguirían metodologías y mejores prácticas bien establecidas en la industria.

### 7.2 Viabilidad Operacional

Desde perspectiva operacional, SkyGate proporciona las funcionalidades fundamentales requeridas para gestionar gates aeroportuarios. Las mejoras propuestas expandirían estas capacidades para manejar situaciones más complejas y proporcionar las robustez requerida para operaciones de producción.

### 7.3 Viabilidad Económica

El costo de implementación y mantenimiento de SkyGate es significativamente menor que alternativas comerciales, mientras que proporciona funcionalidad comparable. Esto lo hace particularmente atractivo para aeropuertos que buscan modernizar sus operaciones con presupuestos limitados.

### 7.4 Aplicabilidad en el Sector

SkyGate demuestra cómo conceptos teóricos de teoría de autómatas pueden aplicarse efectivamente a problemas reales de ingeniería. El sector aeroportuario se beneficiaría de mayor adopción de enfoques formales en el diseño de sistemas críticos, mejorando confiabilidad y facilitando verificación de correctitud.

### 7.5 Recomendación Final

Se recomienda proceder con la implementación gradual de las mejoras propuestas, comenzando con Fase 1 para establecer fundamentos sólidos, continuando con Fase 2 para optimización y cumplimiento regulatorio, y evaluando la necesidad de Fase 3 basándose en el crecimiento proyectado del aeropuerto y requisitos operacionales.

El proyecto SkyGate representa un excelente ejemplo de aplicación práctica de teoría de autómatas y tiene potencial real para generar valor en operaciones aeroportuarias reales.

---

**Documento elaborado por:** Julián Espitia, Mónica Vellojín
**Clasificación:** Mejoras y Recomendaciones Finales