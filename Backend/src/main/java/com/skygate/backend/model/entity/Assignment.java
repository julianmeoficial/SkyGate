package com.skygate.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Flight is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @NotNull(message = "Gate is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gate_id", nullable = false)
    private Gate gate;

    @NotNull(message = "Assigned at is required")
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "expected_arrival")
    private LocalDateTime expectedArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "led_activated", nullable = false)
    private Boolean ledActivated;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Assignment() {
        this.assignedAt = LocalDateTime.now();
        this.isActive = true;
        this.ledActivated = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Assignment(Flight flight, Gate gate) {
        this();
        this.flight = flight;
        this.gate = gate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
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
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getLedActivated() {
        return ledActivated;
    }

    public void setLedActivated(Boolean ledActivated) {
        this.ledActivated = ledActivated;
        this.updatedAt = LocalDateTime.now();
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

    public void activateLeds() {
        this.ledActivated = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivateLeds() {
        this.ledActivated = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.isActive = false;
        this.departureTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", flight=" + flight.getFlightNumber() +
                ", gate=" + gate.getGateNumber() +
                ", assignedAt=" + assignedAt +
                ", isActive=" + isActive +
                '}';
    }
}
