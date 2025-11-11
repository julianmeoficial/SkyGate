package com.skygate.backend.model.enums;

public enum AutomataInput {
    I1("I1", "Sensor detecta aeronave en pista de rodaje", "Deteccion inicial"),
    I2("I2", "Consulta exitosa a BD modelo identificado", "Confirmacion de tipo"),
    I3("I3", "Gate compatible disponible", "Gate encontrado"),
    I4("I4", "Gate compatible NO disponible", "Sin gates"),
    I5("I5", "Aeronave llego al gate", "Arribo confirmado"),
    I6("I6", "Aeronave salio del gate", "Salida confirmada"),
    OTHER("OTHER", "Entrada no reconocida", "Mantiene estado actual");

    private final String code;
    private final String description;
    private final String displayName;

    AutomataInput(String code, String description, String displayName) {
        this.code = code;
        this.description = description;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AutomataInput fromString(String input) {
        if (input == null) {
            return OTHER;
        }
        for (AutomataInput automataInput : AutomataInput.values()) {
            if (automataInput.name().equalsIgnoreCase(input) ||
                    automataInput.code.equalsIgnoreCase(input)) {
                return automataInput;
            }
        }
        return OTHER;
    }

    public boolean isDetectionInput() {
        return this == I1;
    }

    public boolean isConfirmationInput() {
        return this == I2;
    }

    public boolean isAvailabilityInput() {
        return this == I3 || this == I4;
    }

    public boolean isArrivalInput() {
        return this == I5;
    }

    public boolean isDepartureInput() {
        return this == I6;
    }
}
