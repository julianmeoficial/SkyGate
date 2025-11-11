package com.skygate.backend.exception;

import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.GateType;

public class NoAvailableGateException extends RuntimeException {

    private AircraftType aircraftType;
    private GateType requiredGateType;
    private String flightNumber;

    public NoAvailableGateException(String message) {
        super(message);
    }

    public NoAvailableGateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableGateException(AircraftType aircraftType) {
        super(String.format("No available gates found for aircraft type: %s", aircraftType.getDisplayName()));
        this.aircraftType = aircraftType;
    }

    public NoAvailableGateException(AircraftType aircraftType, String flightNumber) {
        super(String.format("No available gates found for flight %s with aircraft type: %s",
                flightNumber, aircraftType.getDisplayName()));
        this.aircraftType = aircraftType;
        this.flightNumber = flightNumber;
    }

    public NoAvailableGateException(GateType requiredGateType, String flightNumber) {
        super(String.format("No available gates of type %s found for flight %s",
                requiredGateType.getDisplayName(), flightNumber));
        this.requiredGateType = requiredGateType;
        this.flightNumber = flightNumber;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public GateType getRequiredGateType() {
        return requiredGateType;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
}
