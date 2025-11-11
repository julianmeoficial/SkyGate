package com.skygate.backend.model.enums;

public enum GateStatus {
    FREE("Free", "Gate disponible para asignacion", "VERDE"),
    ASSIGNED("Assigned", "Gate asignado esperando aeronave", "AMARILLO"),
    OCCUPIED("Occupied", "Gate ocupado con aeronave estacionada", "ROJO"),
    MAINTENANCE("Maintenance", "Gate en mantenimiento no disponible", "NARANJA"),
    RESERVED("Reserved", "Gate reservado para vuelo programado", "AZUL");

    private final String displayName;
    private final String description;
    private final String ledColor;

    GateStatus(String displayName, String description, String ledColor) {
        this.displayName = displayName;
        this.description = description;
        this.ledColor = ledColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getLedColor() {
        return ledColor;
    }

    public boolean isAvailable() {
        return this == FREE || this == RESERVED;
    }

    public boolean isOccupied() {
        return this == OCCUPIED || this == ASSIGNED;
    }

    public static GateStatus fromString(String status) {
        if (status == null) {
            return FREE;
        }
        for (GateStatus gateStatus : GateStatus.values()) {
            if (gateStatus.name().equalsIgnoreCase(status) ||
                    gateStatus.displayName.equalsIgnoreCase(status)) {
                return gateStatus;
            }
        }
        return FREE;
    }
}
