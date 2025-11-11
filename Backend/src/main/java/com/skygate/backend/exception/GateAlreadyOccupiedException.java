package com.skygate.backend.exception;

public class GateAlreadyOccupiedException extends RuntimeException {

    private String gateNumber;
    private Long gateId;
    private String occupiedByFlight;

    public GateAlreadyOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GateAlreadyOccupiedException(String gateNumber) {
        super(String.format("Gate %s is already occupied", gateNumber));
        this.gateNumber = gateNumber;
    }

    public GateAlreadyOccupiedException(String gateNumber, String occupiedByFlight) {
        super(String.format("Gate %s is already occupied by flight %s", gateNumber, occupiedByFlight));
        this.gateNumber = gateNumber;
        this.occupiedByFlight = occupiedByFlight;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public Long getGateId() {
        return gateId;
    }

    public String getOccupiedByFlight() {
        return occupiedByFlight;
    }
}
