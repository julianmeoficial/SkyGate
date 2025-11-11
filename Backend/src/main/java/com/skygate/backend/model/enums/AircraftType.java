package com.skygate.backend.model.enums;

public enum AircraftType {
    WIDE_BODY("Wide Body", "Aeronaves de fuselaje ancho como A350, B777"),
    JUMBO("Jumbo", "Aeronaves de gran tamano como A380"),
    NARROW_BODY("Narrow Body", "Aeronaves de fuselaje estrecho como A320 Family"),
    UNKNOWN("Unknown", "Tipo de aeronave desconocido");

    private final String displayName;
    private final String description;

    AircraftType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static AircraftType fromString(String type) {
        if (type == null) {
            return UNKNOWN;
        }
        for (AircraftType aircraftType : AircraftType.values()) {
            if (aircraftType.name().equalsIgnoreCase(type) ||
                    aircraftType.displayName.equalsIgnoreCase(type)) {
                return aircraftType;
            }
        }
        return UNKNOWN;
    }

    public static AircraftType fromGateType(GateType gateType) {
        switch (gateType) {
            case JUMBO:
                return JUMBO;
            case WIDE_BODY:
                return WIDE_BODY;
            case NARROW_BODY:
                return NARROW_BODY;
            default:
                return UNKNOWN;
        }
    }

    public boolean isCompatibleWith(GateType gateType) {
        switch (this) {
            case JUMBO:
                return gateType == GateType.JUMBO;
            case WIDE_BODY:
                return gateType == GateType.WIDE_BODY || gateType == GateType.JUMBO;
            case NARROW_BODY:
                return gateType == GateType.NARROW_BODY ||
                        gateType == GateType.WIDE_BODY ||
                        gateType == GateType.JUMBO;
            default:
                return false;
        }
    }
}
