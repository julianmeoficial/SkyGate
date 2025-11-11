# SkyGate

Sistema de gestión automatizada de gates aeroportuarios basado en un Autómata Finito Determinista (DFA). Controla detección de aeronaves, asignación dinámica de gates y seguimiento de estados en tiempo real.

## Descripción

Aplicación web full-stack que automatiza la gestión de puertas de embarque mediante un DFA de 8 estados. Detecta aeronaves, asigna gates compatibles según tipo, gestiona colas de espera y monitorea estados vía WebSocket.

## Características

- Autómata Finito Determinista (8 estados, 6 inputs)
- Asignación dinámica por tipo de aeronave
- Comunicación tiempo real (WebSocket/STOMP)
- Sistema de colas para vuelos en espera
- Monitoreo de métricas en vivo
- Autenticación JWT

##  Stack Tecnológico

**Backend**: Spring Boot 3.5.6 • Java 17 • PostgreSQL • WebSocket • JWT • Maven

**Frontend**: React 19 • TypeScript 5.9 • Vite 7 • Tailwind CSS • Zustand • TanStack Query • GSAP

## Estructura

```
SkyGate/
├── Backend/               # Spring Boot
│   └── src/main/java/com/skygate/backend/
│       ├── config/
│       ├── controller/
│       ├── model/
│       ├── service/
│       ├── repository/
│       └── websocket/
└── Frontend/             # React + TypeScript
    └── src/
        ├── api/
        ├── components/
        ├── hooks/
        ├── pages/
        ├── store/
        └── types/
```

## Documentación

- **[Backend Documentation](./Backend/BACKEND_DOCUMENTATION.md)** - Arquitectura, API, DFA, WebSocket
- **[Frontend Documentation](./Frontend/FRONTEND_DOCUMENTATION.md)** - Componentes, hooks, estado, diseño

## Instalación

### Requisitos
- Java 17+ • Node.js 18+ • PostgreSQL 14+ • Maven 3.8+

### Backend

cd Backend
mvn clean install
mvn spring-boot:run

Disponible en: `http://localhost:8080`

### Frontend

cd Frontend
npm install
npm run dev

Disponible en: `http://localhost:5173`

##  Configuración

**Backend** (`application.properties`):

spring.datasource.url=jdbc:postgresql://localhost:5432/skygate
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
jwt.secret=tu_secreto_jwt

**Frontend** (`.env`):

VITE_API_URL=http://localhost:8080/api/v1
VITE_WS_URL=http://localhost:8080/ws

##  Autómata DFA

**Estados**: S0 (Inicializado) → S1 (Detectada) → S2 (Tipo confirmado) → S3 (Buscando gate) → S4 (Asignado) → S5 (Estacionada) → S6 (Espera) → S7 (Partió)

**Inputs**: I1 (Detección) • I2 (Confirmación tipo) • I3 (Gate disponible) • I4 (Sin gates) • I5 (Arribo) • I6 (Partida)

## Roadmap

- Integración MQTT (Arduino/ESP32)
- Notificaciones push
- Dashboard analytics
- Optimización ML
- Modo PWA

## Autores

**Julián Espitia** - [@julianmeoficial](https://github.com/julianmeoficial)  
**Mónica Vellojin**

## Licencia

Proyecto académico - Universidad de Cartagena  
Teoría de Autómatas (TDA) • 5/10 Ingeniería de Software

## Contacto

📧 jmartineze@unicartagena.edu.co • mvellojinm@unicartagena.edu.co  
💼 [LinkedIn](https://linkedin.com/in/julianmeoficial) • [GitHub](https://github.com/julianmeoficial)
