package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Assignment;
import java.time.LocalDateTime;

public class AssignmentResponseDTO {

    private Long id;
    private FlightSimpleDTO flight;
    private GateSimpleDTO gate;
    private LocalDateTime assignedAt;
    private LocalDateTime expectedArrival;
    private LocalDateTime actualArrival;
    private LocalDateTime departureTime;
    private Boolean isActive;
    private Boolean ledActivated;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssignmentResponseDTO() {
    }

    public AssignmentResponseDTO(Assignment assignment) {
        this.id = assignment.getId();
        this.flight = new FlightSimpleDTO(assignment.getFlight());
        this.gate = new GateSimpleDTO(assignment.getGate());
        this.assignedAt = assignment.getAssignedAt();
        this.expectedArrival = assignment.getExpectedArrival();
        this.actualArrival = assignment.getActualArrival();
        this.departureTime = assignment.getDepartureTime();
        this.isActive = assignment.getIsActive();
        this.ledActivated = assignment.getLedActivated();
        this.notes = assignment.getNotes();
        this.createdAt = assignment.getCreatedAt();
        this.updatedAt = assignment.getUpdatedAt();
    }

    public static AssignmentResponseDTO fromEntity(Assignment assignment) {
        return new AssignmentResponseDTO(assignment);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlightSimpleDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightSimpleDTO flight) {
        this.flight = flight;
    }

    public GateSimpleDTO getGate() {
        return gate;
    }

    public void setGate(GateSimpleDTO gate) {
        this.gate = gate;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getExpectedArrival() {
        return expectedArrival;
    }

    public void setExpectedArrival(LocalDateTime expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getLedActivated() {
        return ledActivated;
    }

    public void setLedActivated(Boolean ledActivated) {
        this.ledActivated = ledActivated;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "AssignmentResponseDTO{" +
                "id=" + id +
                ", flight=" + flight.getFlightNumber() +
                ", gate=" + gate.getGateNumber() +
                ", isActive=" + isActive +
                ", ledActivated=" + ledActivated +
                '}';
    }
}
