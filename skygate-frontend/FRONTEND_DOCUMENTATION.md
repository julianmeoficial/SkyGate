# Documentación del Frontend - SkyGate

## Tabla de Contenidos

1. [Introducción](#introducción)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
4. [Estructura de Paquetes](#estructura-de-paquetes)
5. [Conexión con Backend](#conexión-con-backend)
6. [Principios de Diseño](#principios-de-diseño)
7. [Metodología de Diseño](#metodología-de-diseño)
8. [Integración con GSAP](#integración-con-gsap)
9. [Mejoras Futuras](#mejoras-futuras)

## Introducción

SkyGate es una aplicación web desarrollada en React con TypeScript que proporciona una interfaz de usuario moderna y reactiva para la gestión de puertas de embarque de aeropuertos. El sistema permite monitorear en tiempo real el estado de vuelos, puertas, asignaciones y el autómata que gestiona el proceso de asignación automática.

## Stack Tecnológico

### Core Framework y Lenguaje
- **React 19.1.1**: Biblioteca para construir interfaces de usuario
- **TypeScript 5.9.3**: Superset de JavaScript con tipado estático
- **Vite 7.1.7**: Build tool y servidor de desarrollo de alto rendimiento

### Gestión de Estado
- **Zustand 5.0.8**: Librería ligera para gestión de estado global
- **TanStack React Query 5.90.5**: Gestión de estado del servidor, caché y sincronización

### Enrutamiento
- **React Router DOM 7.9.5**: Enrutamiento declarativo para aplicaciones React

### Estilos y Diseño
- **Tailwind CSS 3.4.18**: Framework de utilidades CSS para diseño rápido
- **PostCSS 8.5.6**: Herramienta de transformación de CSS
- **Autoprefixer 10.4.21**: Plugin de PostCSS para añadir prefijos de navegadores

### Comunicación con Backend
- **Axios 1.13.1**: Cliente HTTP para realizar peticiones a la API
- **SockJS Client 1.6.1**: Cliente WebSocket para conexiones bidireccionales
- **@stomp/stompjs 7.2.1**: Implementación de STOMP sobre WebSocket

### Animaciones
- **GSAP 3.13.0**: Biblioteca de animaciones de alto rendimiento

### Componentes UI
- **@headlessui/react 2.2.9**: Componentes UI sin estilos predefinidos
- **@heroicons/react 2.2.0**: Iconos SVG optimizados para React
- **Recharts 3.3.0**: Biblioteca de gráficos para React

### Utilidades
- **clsx 2.1.1**: Utilidad para construir strings de clases condicionalmente
- **date-fns 4.1.0**: Librería de utilidades para manipulación de fechas

### Herramientas de Desarrollo
- **ESLint 9.36.0**: Linter para mantener calidad de código
- **TypeScript ESLint 8.45.0**: Reglas de ESLint específicas para TypeScript
- **React Query Devtools 5.90.2**: Herramientas de desarrollo para React Query

## Arquitectura del Proyecto

### Patrón de Arquitectura

El proyecto sigue una arquitectura modular basada en capas, separando las responsabilidades en:

1. **Capa de Presentación**: Componentes React y páginas
2. **Capa de Lógica de Negocio**: Hooks personalizados y servicios
3. **Capa de Datos**: Servicios API, WebSocket y gestión de estado
4. **Capa de Configuración**: Configuración de herramientas y variables de entorno

### Flujo de Datos

```
Usuario → Componente → Hook → Servicio → API/WebSocket → Backend
                ↓
            Estado Global (Zustand)
                ↓
            React Query Cache
```

### Principios Arquitectónicos

- **Separación de Concerns**: Cada módulo tiene una responsabilidad específica
- **Componentización**: Componentes reutilizables y modulares
- **Inmutabilidad**: El estado se maneja de forma inmutable
- **Unidireccionalidad**: Flujo de datos unidireccional (React patterns)
- **Código Declarativo**: Uso de JSX y hooks para código más legible

## Estructura de Paquetes

```
skygate-frontend/
├── src/
│   ├── api/                    # Capa de comunicación con backend
│   │   ├── axios.config.ts     # Configuración de Axios
│   │   ├── endpoints.ts        # Definición de endpoints
│   │   ├── index.ts            # Exportaciones públicas
│   │   ├── services/           # Servicios de API
│   │   │   ├── assignment.service.ts
│   │   │   ├── flight.service.ts
│   │   │   ├── gate.service.ts
│   │   │   ├── monitoring.service.ts
│   │   │   └── index.ts
│   │   └── websocket/          # Configuración WebSocket
│   │       ├── socket.config.ts
│   │       ├── useWebSocket.ts
│   │       └── index.ts
│   │
│   ├── assets/                 # Recursos estáticos
│   │   ├── icons/              # Iconos del sistema
│   │   └── images/             # Imágenes
│   │
│   ├── components/             # Componentes React
│   │   ├── airport/            # Componentes de aeropuerto
│   │   ├── assignments/        # Componentes de asignaciones
│   │   ├── automata/           # Componentes del autómata
│   │   ├── common/             # Componentes comunes reutilizables
│   │   │   ├── Badge/
│   │   │   ├── Button/
│   │   │   ├── Card/
│   │   │   ├── Icon/
│   │   │   ├── Input/
│   │   │   ├── Listbox/
│   │   │   └── Loader/
│   │   ├── dashboard/          # Componentes del dashboard
│   │   ├── flights/            # Componentes de vuelos
│   │   ├── gates/              # Componentes de puertas
│   │   ├── layout/             # Componentes de layout
│   │   ├── monitoring/         # Componentes de monitoreo
│   │   └── notifications/      # Componentes de notificaciones
│   │
│   ├── config/                 # Configuración
│   │   ├── env.config.ts       # Variables de entorno
│   │   ├── queryClient.config.ts # Configuración React Query
│   │   └── index.ts
│   │
│   ├── hooks/                  # Hooks personalizados
│   │   ├── useAnimateTransition.ts
│   │   ├── useAssignments.ts
│   │   ├── useAutomata.ts
│   │   ├── useAutomataAnimations.ts
│   │   ├── useDebounce.ts
│   │   ├── useFlights.ts
│   │   ├── useGates.ts
│   │   ├── useLocalStorage.ts
│   │   ├── useNotification.ts
│   │   └── index.ts
│   │
│   ├── modal/                  # Modales
│   │   ├── GateHistoryModal.tsx
│   │   ├── MaintenanceModal.tsx
│   │   └── index.ts
│   │
│   ├── pages/                  # Páginas/Vistas
│   │   ├── AssignmentsView/
│   │   ├── AutomataView/
│   │   ├── Dashboard.tsx
│   │   ├── FlightManagement/
│   │   ├── GateManagement/
│   │   ├── MonitoringView/
│   │   ├── ProcessDiagramView/
│   │   └── index.ts
│   │
│   ├── store/                  # Estado global (Zustand)
│   │   ├── slices/
│   │   │   ├── automata.slice.ts
│   │   │   ├── gate.slice.ts
│   │   │   ├── notification.slice.ts
│   │   │   └── index.ts
│   │   └── index.ts
│   │
│   ├── types/                  # Tipos TypeScript
│   │   ├── backend.types.ts    # Tipos del backend
│   │   ├── sockjs-client.d.ts  # Definiciones SockJS
│   │   └── index.ts
│   │
│   ├── utils/                  # Utilidades
│   │   ├── animations/         # Utilidades de animación
│   │   ├── colors.ts           # Utilidades de colores
│   │   ├── constants.ts        # Constantes
│   │   ├── formatters.ts       # Formateadores
│   │   └── index.ts
│   │
│   ├── App.tsx                 # Componente principal
│   ├── App.css                 # Estilos globales
│   ├── main.tsx                # Punto de entrada
│   └── index.css               # Estilos base
│
├── public/                     # Archivos públicos
├── node_modules/               # Dependencias
├── index.html                  # HTML principal
├── package.json                # Dependencias del proyecto
├── package-lock.json           # Lock file de dependencias
├── tsconfig.json               # Configuración TypeScript
├── tsconfig.app.json           # Configuración TypeScript para app
├── tsconfig.node.json          # Configuración TypeScript para Node
├── vite.config.ts              # Configuración Vite
├── tailwind.config.js          # Configuración Tailwind
├── postcss.config.js           # Configuración PostCSS
├── eslint.config.js            # Configuración ESLint
└── .gitignore                  # Archivos ignorados por Git
```

## Conexión con Backend

### Configuración de API REST

La conexión con el backend REST se realiza mediante Axios, configurado en `src/api/axios.config.ts`:

#### Instancia de Axios

```typescript
baseURL: http://localhost:8080/api/v1
timeout: 10000ms
headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer {token}'
}
```

#### Interceptores

**Interceptor de Request:**
- Añade el token de autenticación desde `localStorage` si está disponible
- Registra las peticiones en modo desarrollo

**Interceptor de Response:**
- Maneja errores HTTP (401, 403, 404, 409, 500)
- Redirige al login en caso de token inválido (401)
- Registra respuestas en modo desarrollo

#### Endpoints Definidos

Los endpoints están centralizados en `src/api/endpoints.ts`:

- **Flights**: `/flights`, `/flights/{id}`, `/flights/detect`, `/flights/simulate`
- **Gates**: `/gates`, `/gates/{id}`, `/gates/available`, `/gates/{id}/status`
- **Assignments**: `/assignments`, `/assignments/{id}`, `/assignments/active/details`
- **Monitoring**: `/monitoring/dashboard`, `/monitoring/system-health`
- **Auth**: `/auth/login`, `/auth/validate`

#### Servicios de API

Los servicios están organizados por dominio de negocio:

- `flight.service.ts`: Operaciones relacionadas con vuelos
- `gate.service.ts`: Operaciones relacionadas con puertas
- `assignment.service.ts`: Operaciones relacionadas con asignaciones
- `monitoring.service.ts`: Operaciones de monitoreo

Cada servicio exporta funciones que retornan `Promise<ApiResponse<T>>`, proporcionando tipado fuerte y manejo consistente de respuestas.

### Configuración de WebSocket

La conexión WebSocket se realiza mediante SockJS y STOMP, configurado en `src/api/websocket/`:

#### Configuración STOMP

```typescript
url: http://localhost:8080/ws
reconnectDelay: 5000ms
heartbeatIncoming: 4000ms
heartbeatOutgoing: 4000ms
```

#### Topics Suscritos

- `/topic/automata/transitions`: Transiciones del autómata
- `/topic/flights/updates`: Actualizaciones de vuelos
- `/topic/gates/updates`: Actualizaciones de puertas
- `/topic/assignments/updates`: Actualizaciones de asignaciones

#### Hook useWebSocket

El hook `useWebSocket` proporciona:

- Gestión del estado de conexión (CONNECTING, CONNECTED, DISCONNECTED, ERROR)
- Reconexión automática en caso de desconexión
- Prevención de mensajes duplicados
- Integración con el store de Zustand para actualizar el estado global
- Notificaciones automáticas para eventos importantes

#### Proxy de Desarrollo

Vite está configurado con un proxy para desarrollo:

```typescript
proxy: {
    '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
    },
    '/ws': {
        target: 'ws://localhost:8080',
        ws: true,
        changeOrigin: true,
        secure: false
    }
}
```

### Gestión de Estado del Servidor

React Query se utiliza para:

- **Caché de datos**: Los datos se cachean automáticamente
- **Sincronización**: Refetch automático en eventos de ventana
- **Optimistic Updates**: Actualizaciones optimistas para mejor UX
- **Retry Logic**: Reintentos automáticos en caso de error

Configuración de React Query:

```typescript
staleTime: 5000ms
gcTime: 600000ms (10 minutos)
retry: 3 intentos
refetchOnWindowFocus: true
refetchOnReconnect: true
```

### Variables de Entorno

Las variables de entorno se gestionan mediante Vite:

- `VITE_API_URL`: URL base de la API (default: `http://localhost:8080/api/v1`)
- `VITE_WS_URL`: URL del WebSocket (default: `http://localhost:8080/ws`)
- `VITE_API_TIMEOUT`: Timeout de peticiones (default: `10000`)

## Principios de Diseño

### Diseño Centrado en el Usuario

- **Claridad**: Interfaces claras y comprensibles
- **Consistencia**: Patrones de diseño consistentes en toda la aplicación
- **Feedback**: Retroalimentación inmediata para acciones del usuario
- **Accesibilidad**: Consideraciones de accesibilidad en componentes

### Sistema de Diseño

#### Paleta de Colores

El sistema utiliza una paleta de colores semántica definida en `tailwind.config.js`:

**Estados del Autómata:**
- S0 (Sistema Inicializado): `#64748b` (Slate-500)
- S1 (Wide Body Detectado): `#3b82f6` (Blue-500)
- S2 (Jumbo Detectado): `#a855f7` (Purple-500)
- S3 (Narrow Body Detectado): `#06b6d4` (Cyan-500)
- S4 (Gate Asignado): `#eab308` (Yellow-500)
- S5 (Aeronave Estacionada): `#22c55e` (Green-500)
- S6 (Sin Gates): `#ef4444` (Red-500)

**Estados de Gates:**
- FREE: `#22c55e` (Verde)
- ASSIGNED: `#eab308` (Amarillo)
- OCCUPIED: `#ef4444` (Rojo)
- MAINTENANCE: `#f97316` (Naranja)
- RESERVED: `#3b82f6` (Azul)

**Tipos de Aeronaves:**
- Narrow Body: `#06b6d4` (Cyan)
- Wide Body: `#3b82f6` (Blue)
- Jumbo: `#a855f7` (Purple)

#### Tema Oscuro

La aplicación utiliza un tema oscuro por defecto:

- Background Primary: `#0f172a` (Slate-900)
- Background Secondary: `#1e293b` (Slate-800)
- Background Tertiary: `#334155` (Slate-700)

#### Tipografía

- Sistema de tipografía basado en Tailwind CSS
- Uso de fuentes del sistema para mejor rendimiento
- Jerarquía clara de tamaños y pesos

#### Espaciado

- Sistema de espaciado consistente basado en escala de 4px
- Uso de utilidades de Tailwind para spacing

### Principios de UI/UX

1. **Consistencia Visual**: Componentes reutilizables con estilos consistentes
2. **Feedback Visual**: Animaciones y transiciones para feedback inmediato
3. **Estados Claros**: Estados visuales claros para elementos interactivos
4. **Carga Progresiva**: Loading states y skeleton screens
5. **Mensajes Claros**: Mensajes de error y éxito comprensibles
6. **Navegación Intuitiva**: Navegación clara y accesible

## Metodología de Diseño

### Atomic Design

El proyecto sigue los principios de Atomic Design, organizando los componentes en niveles de complejidad creciente:

#### Átomos (Atoms)

Componentes básicos e indivisibles ubicados en `src/components/common/`:

- **Badge**: Componente de etiqueta para estados
- **Button**: Variantes de botones (GoBack, Hover, Neumorphism)
- **Card**: Tarjetas con gradientes (Explore, Profile)
- **Icon**: Sistema de iconos reutilizable
- **Input**: Campos de entrada (AnimatedCheck, FocusRing, SimpleLabel)
- **Listbox**: Componente de selección
- **Loader**: Variantes de carga (BounceDots, CardSkeleton, DoubleSpinner, GradientBox, TripleCircle)

Características de los átomos:
- Componentes puros sin dependencias de negocio
- Props bien definidas con TypeScript
- Estilos encapsulados
- Altamente reutilizables

#### Moléculas (Molecules)

Componentes compuestos por átomos, ubicados en dominios específicos:

- **InputIndicator**: Indicador de entrada del autómata (compuesto de Icon + Badge)
- **StateIndicator**: Indicador de estado (compuesto de Badge + estilos)
- **GateCard**: Tarjeta de puerta (compuesto de Card + Badge + Icon)
- **FlightCard**: Tarjeta de vuelo (compuesto de Card + Badge + datos)

#### Organismos (Organisms)

Componentes complejos que combinan moléculas y átomos:

- **GatePanel**: Panel de gestión de puertas (compuesto de múltiples GateCard)
- **FlightTable**: Tabla de vuelos (compuesto de múltiples FlightCard)
- **AssignmentTable**: Tabla de asignaciones (compuesto de múltiples componentes)
- **AutomataTimeline**: Línea de tiempo del autómata (compuesto de StateIndicator + InputIndicator)
- **SystemStatus**: Estado del sistema (compuesto de múltiples Badge y Cards)

#### Plantillas (Templates)

Estructuras de página que definen el layout:

- **MainLayout**: Layout principal de la aplicación (Sidebar + Navbar + Main Content)
- **Sidebar**: Barra lateral de navegación
- **Navbar**: Barra de navegación superior

#### Páginas (Pages)

Vistas completas que utilizan plantillas y organismos:

- **Dashboard**: Página principal con métricas y gráficos
- **FlightManagement**: Gestión de vuelos
- **GateManagement**: Gestión de puertas
- **AutomataView**: Vista del autómata
- **AssignmentsView**: Vista de asignaciones
- **MonitoringView**: Vista de monitoreo
- **ProcessDiagramView**: Vista del diagrama de procesos

### Organización de Componentes

Cada componente sigue una estructura consistente:

```
ComponentName/
├── ComponentName.tsx    # Componente principal
├── index.ts             # Exportaciones
└── ComponentName.test.tsx  # Tests (futuro)
```

### Principios de Componentización

1. **Single Responsibility**: Cada componente tiene una responsabilidad única
2. **Composición sobre Herencia**: Preferencia por composición de componentes
3. **Props Bien Definidas**: Interfaces TypeScript para todas las props
4. **Estados Locales**: Estados locales cuando es apropiado
5. **Lifting State Up**: Elevar estado cuando es necesario compartirlo

### Convenciones de Nomenclatura

- **Componentes**: PascalCase (ej: `FlightTable.tsx`)
- **Hooks**: camelCase con prefijo "use" (ej: `useFlights.ts`)
- **Utilidades**: camelCase (ej: `formatters.ts`)
- **Tipos**: PascalCase (ej: `FlightStatus`)
- **Constantes**: UPPER_SNAKE_CASE (ej: `API_ENDPOINTS`)

## Integración con GSAP

### Configuración de GSAP

GSAP (GreenSock Animation Platform) se utiliza para animaciones de alto rendimiento en el proyecto. La librería está integrada en `src/hooks/` y se utiliza principalmente para:

- Animaciones de transiciones de estados del autómata
- Animaciones de LEDs virtuales
- Animaciones de componentes de interfaz
- Animaciones de notificaciones
- Animaciones de métricas y gráficos

### Hooks de Animación

#### useAnimateTransition

Hook para animar transiciones entre estados del autómata:

```typescript
useAnimateTransition(fromState: AutomataState, toState: AutomataState)
```

Características:
- Anima el cambio de escala y sombra del estado anterior
- Anima la aparición del nuevo estado con efectos de glow
- Utiliza timeline de GSAP para secuencias de animación
- Previene animaciones duplicadas con useRef

### Integración con CSS

Las animaciones de GSAP se complementan con animaciones CSS definidas en `tailwind.config.js`:

- `pulse-led`: Animación de pulso para LEDs
- `slide-in-right`: Entrada desde la derecha
- `slide-in-down`: Entrada desde arriba
- `fade-in`: Efecto de desvanecimiento
- `bounce-in`: Efecto de rebote
- `glow-pulse`: Pulso de brillo

### Mejores Prácticas de Animación

1. **Performance**: Utilizar `transform` y `opacity` para animaciones de alto rendimiento
2. **Timing**: Utilizar easing functions apropiadas (ease-out, ease-in-out)
3. **Duración**: Animaciones cortas (300-600 ms) para mejor UX
4. **Reducción de Movimiento**: Respetar preferencias de accesibilidad
5. **Cleanup**: Limpiar animaciones en cleanup de efectos

### Uso en Componentes

Las animaciones GSAP se utilizan principalmente en:

- `AutomataTimeline`: Animaciones de transiciones de estado
- `VirtualLED`: Animaciones de LEDs virtuales
- `GatePanel`: Animaciones de cambio de estado de puertas
- `FlightTable`: Animaciones de aparición de vuelos
- `ToastContainer`: Animaciones de notificaciones

## Mejoras Futuras

### Funcionalidades Pendientes

1. **Sistema de Autenticación Completo**
   - Implementar página de login
   - Gestión de sesiones
   - Roles y permisos
   - Refresh tokens

2. **Testing**
   - Unit tests con Jest y React Testing Library
   - Integration tests
   - E2E tests con Playwright o Cypress
   - Test coverage mínimo del 80%

3. **Optimización de Rendimiento**
   - Lazy loading de rutas y componentes
   - Code splitting
   - Memoización de componentes pesados
   - Virtual scrolling para listas grandes
   - Optimización de imágenes

4. **Accesibilidad**
   - ARIA labels en componentes
   - Navegación por teclado
   - Soporte para lectores de pantalla
   - Contraste de colores mejorado
   - Focus management

5. **Internacionalización**
   - Soporte multi-idioma
   - Traducciones para inglés y español
   - Formateo de fechas y números por región

6. **Mejoras de UI/UX**
   - Modo claro/oscuro toggle
   - Personalización de dashboard
   - Filtros avanzados
   - Búsqueda global
   - Atajos de teclado

7. **Manejo de Errores**
   - Error boundaries
   - Páginas de error personalizadas
   - Logging de errores (Sentry)
   - Retry automático con backoff exponencial

8. **Documentación**
   - Storybook para componentes
   - Guías de contribución
   - Documentación de hooks personalizados

9. **DevOps**
   - CI/CD pipeline
   - Dockerización
   - Deploy automatizado
   - Monitoring y alerting

10. **Optimizaciones de Backend Integration**
    - Paginación en listas
    - Infinite scroll
    - Optimistic updates mejorados
    - Cache invalidation más granular

11. **Analytics**
    - Tracking de eventos de usuario
    - Métricas de rendimiento
    - Análisis de uso
    - Heatmaps

12. **Notificaciones Push**
    - Service Workers
    - Notificaciones del navegador
    - Configuración de preferencias

### Mejoras Técnicas

1. **TypeScript**
   - Estricter type checking
   - Eliminación de `any` types
   - Tipos más específicos para props
   - Utility types mejorados

2. **Estado Global**
   - Migración a Redux Toolkit (si es necesario)
   - Normalización de estado
   - Middleware para logging

3. **Formularios**
   - React Hook Form para formularios complejos
   - Validación con Zod o Yup
   - Manejo de errores de formulario

4. **Gráficos**
   - Más tipos de gráficos
   - Interactividad mejorada
   - Exportación de datos
   - Filtros temporales

5. **Animaciones**
   - Implementación de animaciones GSAP faltantes
   - Transiciones de página
   - Micro-interacciones
   - Animaciones de carga más atractivas

6. **Responsive Design**
   - Diseño móvil completo
   - Tablets optimization
   - Touch gestures
   - Progressive Web App (PWA)

7. **Security**
   - Content Security Policy
   - XSS protection
   - CSRF tokens
   - Sanitización de inputs

8. **Performance Monitoring**
   - Web Vitals tracking
   - Performance budgets
   - Bundle size optimization
   - Lighthouse CI

### Refactorización

1. **Componentes**
   - Extraer lógica de negocio de componentes
   - Mejorar reutilización
   - Simplificar componentes complejos
   - Eliminar código duplicado

2. **Hooks**
   - Crear más hooks personalizados
   - Mejorar separación de concerns
   - Optimizar re-renders

3. **Servicios**
   - Mejorar manejo de errores
   - Añadir retry logic
   - Mejorar tipos de respuesta

4. **Estilos**
   - Consolidar estilos duplicados
   - Mejorar sistema de diseño
   - Crear tema configurable

## Conclusión

SkyGate es una aplicación moderna construida con las mejores prácticas de desarrollo web. Utiliza un stack tecnológico robusto, sigue principios de diseño sólidos y mantiene una arquitectura escalable. Con las mejoras futuras planificadas, la aplicación continuará evolucionando para proporcionar una experiencia de usuario excepcional y un código mantenible.

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

---

**Última actualización**: Noviembre 2025
**Versión**: 1.1.0 (stable)
