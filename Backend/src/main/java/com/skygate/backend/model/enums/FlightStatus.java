package com.skygate.backend.model.enums;

public enum FlightStatus {
    DETECTED("Detected", "Vuelo detectado en pista de rodaje"),
    CONFIRMED("Confirmed", "Tipo de aeronave confirmado en BD"),
    GATE_ASSIGNED("Gate Assigned", "Gate asignado esperando llegada"),
    APPROACHING("Approaching", "Aeronave en camino al gate"),
    ARRIVED("Arrived", "Aeronave llego al gate"),
    PARKED("Parked", "Aeronave estacionada en el gate"),
    DEPARTED("Departed", "Aeronave salio del gate"),
    WAITING("Waiting", "Esperando disponibilidad de gate"),
    CANCELLED("Cancelled", "Vuelo cancelado");

    private final String displayName;
    private final String description;

    FlightStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this != DEPARTED && this != CANCELLED;
    }

    public boolean isCompleted() {
        return this == DEPARTED || this == CANCELLED;
    }

    public static FlightStatus fromString(String status) {
        if (status == null) {
            return DETECTED;
        }
        for (FlightStatus flightStatus : FlightStatus.values()) {
            if (flightStatus.name().equalsIgnoreCase(status) ||
                    flightStatus.displayName.equalsIgnoreCase(status)) {
                return flightStatus;
            }
        }
        return DETECTED;
    }

    public static FlightStatus fromAutomataState(AutomataState automataState) {
        switch (automataState) {
            case S0:
                return DETECTED;
            case S1:
            case S2:
            case S3:
                return CONFIRMED;
            case S4:
                return GATE_ASSIGNED;
            case S5:
                return PARKED;
            case S6:
                return WAITING;
            default:
                return DETECTED;
        }
    }
}
