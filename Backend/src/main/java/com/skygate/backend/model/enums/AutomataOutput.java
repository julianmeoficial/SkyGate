package com.skygate.backend.model.enums;

public enum AutomataOutput {
    O1("O1", "LEDs ruta hacia gate especifico", "Activar LEDs de guia", "LED_PATH"),
    O2("O2", "LED verde en gate asignado", "Indicar gate asignado", "LED_GREEN"),
    O3("O3", "LED rojo gate ocupado", "Marcar gate ocupado", "LED_RED"),
    O4("O4", "Display mostrando ESPERAR", "Mensaje de espera", "DISPLAY_WAIT"),
    O5("O5", "Actualizacion en BD gate ocupado o libre", "Sincronizar base de datos", "DB_UPDATE"),
    NONE("NONE", "Sin salida", "No se genera salida", "NO_OUTPUT");

    private final String code;
    private final String description;
    private final String displayName;
    private final String actionCode;

    AutomataOutput(String code, String description, String displayName, String actionCode) {
        this.code = code;
        this.description = description;
        this.displayName = displayName;
        this.actionCode = actionCode;
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

    public String getActionCode() {
        return actionCode;
    }

    public static AutomataOutput fromString(String output) {
        if (output == null) {
            return NONE;
        }
        for (AutomataOutput automataOutput : AutomataOutput.values()) {
            if (automataOutput.name().equalsIgnoreCase(output) ||
                    automataOutput.code.equalsIgnoreCase(output)) {
                return automataOutput;
            }
        }
        return NONE;
    }

    public boolean isLedOutput() {
        return this == O1 || this == O2 || this == O3;
    }

    public boolean isDatabaseOutput() {
        return this == O5;
    }

    public boolean isDisplayOutput() {
        return this == O4;
    }
}
