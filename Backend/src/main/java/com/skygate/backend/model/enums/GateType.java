package com.skygate.backend.model.enums;

public enum GateType {
    JUMBO("Jumbo Gate", "Gate exclusivo para A380 y aeronaves de gran tamano", "G-J"),
    WIDE_BODY("Wide Body Gate", "Gate para aeronaves de fuselaje ancho", "G-W"),
    NARROW_BODY("Narrow Body Gate", "Gate para aeronaves de fuselaje estrecho", "G-N");

    private final String displayName;
    private final String description;
    private final String prefix;

    GateType(String displayName, String description, String prefix) {
        this.displayName = displayName;
        this.description = description;
        this.prefix = prefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrefix() {
        return prefix;
    }

    public static GateType fromAircraftType(AircraftType aircraftType) {
        switch (aircraftType) {
            case JUMBO:
                return JUMBO;
            case WIDE_BODY:
                return WIDE_BODY;
            case NARROW_BODY:
                return NARROW_BODY;
            default:
                return NARROW_BODY;
        }
    }

    public static GateType fromString(String type) {
        if (type == null) {
            return NARROW_BODY;
        }
        for (GateType gateType : GateType.values()) {
            if (gateType.name().equalsIgnoreCase(type) ||
                    gateType.displayName.equalsIgnoreCase(type)) {
                return gateType;
            }
        }
        return NARROW_BODY;
    }
}
