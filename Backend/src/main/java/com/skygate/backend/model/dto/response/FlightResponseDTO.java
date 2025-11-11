package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import java.time.LocalDateTime;

public class FlightResponseDTO {

    private Long id;
    private String flightNumber;
    private AircraftSimpleDTO aircraft;
    private FlightStatus status;
    private AutomataState automataState;
    private String origin;
    private String destination;
    private String airline;
    private LocalDateTime scheduledArrival;
    private LocalDateTime actualArrival;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime actualDeparture;
    private LocalDateTime detectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FlightResponseDTO() {
    }

    public FlightResponseDTO(Flight flight) {
        this.id = flight.getId();
        this.flightNumber = flight.getFlightNumber();
        this.aircraft = new AircraftSimpleDTO(flight.getAircraft());
        this.status = flight.getStatus();
        this.automataState = flight.getAutomataState();
        this.origin = flight.getOrigin();
        this.destination = flight.getDestination();
        this.airline = flight.getAirline();
        this.scheduledArrival = flight.getScheduledArrival();
        this.actualArrival = flight.getActualArrival();
        this.scheduledDeparture = flight.getScheduledDeparture();
        this.actualDeparture = flight.getActualDeparture();
        this.detectedAt = flight.getDetectedAt();
        this.createdAt = flight.getCreatedAt();
        this.updatedAt = flight.getUpdatedAt();
    }

    public static FlightResponseDTO fromEntity(Flight flight) {
        return new FlightResponseDTO(flight);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public AircraftSimpleDTO getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftSimpleDTO aircraft) {
        this.aircraft = aircraft;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public AutomataState getAutomataState() {
        return automataState;
    }

    public void setAutomataState(AutomataState automataState) {
        this.automataState = automataState;
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

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "FlightResponseDTO{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", status=" + status +
                ", automataState=" + automataState +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
