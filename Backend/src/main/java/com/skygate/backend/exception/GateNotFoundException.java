package com.skygate.backend.exception;

public class GateNotFoundException extends RuntimeException {

    private String gateNumber;
    private Long gateId;

    public GateNotFoundException(String message) {
        super(message);
    }

    public GateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GateNotFoundException(Long gateId) {
        super(String.format("Gate with ID %d not found", gateId));
        this.gateId = gateId;
    }

    public GateNotFoundException(String gateNumber, boolean byNumber) {
        super(String.format("Gate with number %s not found", gateNumber));
        this.gateNumber = gateNumber;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public Long getGateId() {
        return gateId;
    }
}
