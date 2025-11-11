package com.skygate.backend.exception;

public class AssignmentNotFoundException extends RuntimeException {

    private Long assignmentId;
    private Long flightId;
    private Long gateId;

    public AssignmentNotFoundException(String message) {
        super(message);
    }

    public AssignmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssignmentNotFoundException(Long assignmentId) {
        super(String.format("Assignment with ID %d not found", assignmentId));
        this.assignmentId = assignmentId;
    }

    public AssignmentNotFoundException(Long flightId, Long gateId) {
        super(String.format("No active assignment found for flight ID %d and gate ID %d", flightId, gateId));
        this.flightId = flightId;
        this.gateId = gateId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public Long getGateId() {
        return gateId;
    }
}
