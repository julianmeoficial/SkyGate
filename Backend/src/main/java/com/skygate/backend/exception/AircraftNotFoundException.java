package com.skygate.backend.exception;

public class AircraftNotFoundException extends RuntimeException {

    private String aircraftModel;
    private Long aircraftId;

    public AircraftNotFoundException(String message) {
        super(message);
    }

    public AircraftNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AircraftNotFoundException(Long aircraftId) {
        super(String.format("Aircraft with ID %d not found", aircraftId));
        this.aircraftId = aircraftId;
    }

    public AircraftNotFoundException(String aircraftModel, boolean byModel) {
        super(String.format("Aircraft with model %s not found", aircraftModel));
        this.aircraftModel = aircraftModel;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public Long getAircraftId() {
        return aircraftId;
    }
}
