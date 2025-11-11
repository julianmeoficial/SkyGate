export const APP_NAME = 'SkyGate';
export const APP_VERSION = '1.0.0';

export const REFRESH_INTERVALS = {
    FAST: 3000,    // 3 segundos
    NORMAL: 5000,  // 5 segundos
    SLOW: 10000,   // 10 segundos
};

export const AIRCRAFT_TYPE_LABELS = {
    NARROW_BODY: 'Narrow Body',
    WIDE_BODY: 'Wide Body',
    JUMBO: 'Jumbo',
    UNKNOWN: 'Desconocido',
};

export const GATE_STATUS_LABELS = {
    FREE: 'Disponible',
    ASSIGNED: 'Asignado',
    OCCUPIED: 'Ocupado',
    MAINTENANCE: 'Mantenimiento',
    RESERVED: 'Reservado',
};

export const STATE_DESCRIPTIONS = {
    S0: 'Sistema Inicializado',
    S1: 'Wide Body Detectado',
    S2: 'Jumbo Detectado',
    S3: 'Narrow Body Detectado',
    S4: 'Gate Asignado',
    S5: 'Aeronave Estacionada',
    S6: 'Sin Gates Disponibles',
};
