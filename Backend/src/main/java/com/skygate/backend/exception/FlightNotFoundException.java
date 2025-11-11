package com.skygate.backend.exception;

public class FlightNotFoundException extends RuntimeException {

    private String flightNumber;
    private Long flightId;

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlightNotFoundException(Long flightId) {
        super(String.format("Flight with ID %d not found", flightId));
        this.flightId = flightId;
    }

    public FlightNotFoundException(String flightNumber, boolean byNumber) {
        super(String.format("Flight with number %s not found", flightNumber));
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Long getFlightId() {
        return flightId;
    }
}
