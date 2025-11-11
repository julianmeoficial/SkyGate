package com.skygate.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class FlightRequestDTO {

    @NotBlank(message = "Flight number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{3,4}$", message = "Flight number must follow format: 2 letters + 3-4 digits (e.g., AV123)")
    private String flightNumber;

    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    @Size(max = 100, message = "Origin must not exceed 100 characters")
    private String origin;

    @Size(max = 100, message = "Destination must not exceed 100 characters")
    private String destination;

    @Size(max = 50, message = "Airline must not exceed 50 characters")
    private String airline;

    private LocalDateTime scheduledArrival;

    private LocalDateTime scheduledDeparture;

    public FlightRequestDTO() {
    }

    public FlightRequestDTO(String flightNumber, Long aircraftId, String origin, String destination) {
        this.flightNumber = flightNumber;
        this.aircraftId = aircraftId;
        this.origin = origin;
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    @Override
    public String toString() {
        return "FlightRequestDTO{" +
                "flightNumber='" + flightNumber + '\'' +
                ", aircraftId=" + aircraftId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
