package com.skygate.backend.model.enums;

public enum AutomataState {
    S0("S0", "Sistema inicializado", "Todos los gates libres, esperando deteccion", true),
    S1("S1", "Wide Body detectado", "Buscando gate compatible para Wide Body", false),
    S2("S2", "Jumbo detectado", "Verificando gate Jumbo disponible", false),
    S3("S3", "Narrow Body detectado", "Asignando cualquier gate libre", false),
    S4("S4", "Gate asignado", "LEDs activados guiando aeronave", false),
    S5("S5", "Aeronave estacionada", "Gate ocupado", true),
    S6("S6", "Conflicto", "No hay gates compatibles disponibles", false);

    private final String code;
    private final String displayName;
    private final String description;
    private final boolean isFinalState;

    AutomataState(String code, String displayName, String description, boolean isFinalState) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
        this.isFinalState = isFinalState;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFinalState() {
        return isFinalState;
    }

    public boolean isInitialState() {
        return this == S0;
    }

    public boolean isDetectionState() {
        return this == S1 || this == S2 || this == S3;
    }

    public boolean isConflictState() {
        return this == S6;
    }

    public static AutomataState fromString(String state) {
        if (state == null) {
            return S0;
        }
        for (AutomataState automataState : AutomataState.values()) {
            if (automataState.name().equalsIgnoreCase(state) ||
                    automataState.code.equalsIgnoreCase(state)) {
                return automataState;
            }
        }
        return S0;
    }

    public static AutomataState getDetectionState(AircraftType aircraftType) {
        switch (aircraftType) {
            case WIDE_BODY:
                return S1;
            case JUMBO:
                return S2;
            case NARROW_BODY:
                return S3;
            default:
                return S0;
        }
    }
}
