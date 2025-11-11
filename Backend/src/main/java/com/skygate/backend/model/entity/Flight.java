package com.skygate.backend.model.entity;

import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flight number is required")
    @Column(name = "flight_number", nullable = false, unique = true, length = 20)
    private String flightNumber;

    @NotNull(message = "Aircraft is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @NotNull(message = "Flight status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FlightStatus status;

    @NotNull(message = "Automata state is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "automata_state", nullable = false, length = 10)
    private AutomataState automataState;

    @Column(name = "origin", length = 100)
    private String origin;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "airline", length = 50)
    private String airline;

    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture;

    @Column(name = "actual_departure")
    private LocalDateTime actualDeparture;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments;

    public Flight() {
        this.status = FlightStatus.DETECTED;
        this.automataState = AutomataState.S0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.detectedAt = LocalDateTime.now();
        this.assignments = new ArrayList<>();
    }

    public Flight(String flightNumber, Aircraft aircraft, String origin, String destination) {
        this();
        this.flightNumber = flightNumber;
        this.aircraft = aircraft;
        this.origin = origin;
        this.destination = destination;
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

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public AutomataState getAutomataState() {
        return automataState;
    }

    public void setAutomataState(AutomataState automataState) {
        this.automataState = automataState;
        this.updatedAt = LocalDateTime.now();
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

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void transitionToState(AutomataState newState) {
        this.automataState = newState;
        this.status = FlightStatus.fromAutomataState(newState);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status.isActive();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", aircraft=" + aircraft.getModel() +
                ", status=" + status +
                ", automataState=" + automataState +
                ", origin='" + origin + '\'' +
                '}';
    }
}
