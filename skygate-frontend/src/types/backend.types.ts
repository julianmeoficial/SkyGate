export type AircraftType = 'WIDE_BODY' | 'JUMBO' | 'NARROW_BODY' | 'UNKNOWN';

export type FlightStatus =
    | 'DETECTED'
    | 'CONFIRMED'
    | 'GATE_ASSIGNED'
    | 'APPROACHING'
    | 'ARRIVED'
    | 'PARKED'
    | 'DEPARTED'
    | 'WAITING'
    | 'CANCELLED';

export type GateStatus = 'FREE' | 'ASSIGNED' | 'OCCUPIED' | 'MAINTENANCE' | 'RESERVED';

export type GateType = 'JUMBO' | 'WIDE_BODY' | 'NARROW_BODY';

export type AutomataState = 'S0' | 'S1' | 'S2' | 'S3' | 'S4' | 'S5' | 'S6';

export type AutomataInput = 'I1' | 'I2' | 'I3' | 'I4' | 'I5' | 'I6';

export type AutomataOutput = 'O1' | 'O2' | 'O3' | 'O4' | 'O5';

export interface ApiResponse<T = any> {
    success: boolean;
    message: string;
    data: T;
    timestamp?: string;
    error?: {
        code: string;
        details: string;
    };
}

export interface Flight {
    id: number;
    flightNumber: string;
    aircraftType: AircraftType;
    aircraft?: string;
    status: FlightStatus;
    automataState: AutomataState;
    origin: string;
    destination: string;
    airline: string;
    scheduledArrival?: string;
    actualArrival?: string;
    scheduledDeparture?: string;
    actualDeparture?: string;
    detectedAt?: string;
    createdAt: string;
    updatedAt: string;
}

export interface Gate {
    id: number;
    gateNumber: string;
    gateType: GateType;
    status: GateStatus;
    location: string;
    terminal: string;
    ledPath?: string;
    isActive: boolean;
    createdAt: string;
    updatedAt: string;
}

export interface Assignment {
    id: number;
    flight: Flight;
    gate: Gate;
    assignedAt: string;
    expectedArrival?: string;
    actualArrival?: string;
    departureTime?: string;
    isActive: boolean;
    ledActivated: boolean;
    notes?: string;
    createdAt: string;
    updatedAt: string;
}

export interface DashboardStats {
    gates: {
        total: number;
        free: number;
        occupied: number;
        assigned: number;
        maintenance: number;
    };
    flights: {
        total: number;
        active: number;
        detected: number;
        gateAssigned: number;
        parked: number;
        departed: number;
    };
    automataStates: {
        s0: number;
        s1: number;
        s2: number;
        s3: number;
        s4: number;
        s5: number;
        s6: number;
    };
    assignments: {
        active: number;
        ledsActivated: number;
    };
}

export interface AutomataTransition {
    flightId: number;
    flightNumber: string;
    fromState: AutomataState;
    toState: AutomataState;
    input: AutomataInput;
    outputs?: AutomataOutput[];
    timestamp: string;
    event?: string;
}

export interface WebSocketMessage {
    type: 'AUTOMATA_TRANSITION' | 'FLIGHT_UPDATE' | 'GATE_UPDATE' | 'ASSIGNMENT_UPDATE';
    payload: any;
    timestamp: string;
}
