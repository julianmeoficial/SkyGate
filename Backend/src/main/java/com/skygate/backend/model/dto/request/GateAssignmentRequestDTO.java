package com.skygate.backend.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class GateAssignmentRequestDTO {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Gate ID is required")
    private Long gateId;

    private LocalDateTime expectedArrival;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    public GateAssignmentRequestDTO() {
    }

    public GateAssignmentRequestDTO(Long flightId, Long gateId) {
        this.flightId = flightId;
        this.gateId = gateId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getGateId() {
        return gateId;
    }

    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

    public LocalDateTime getExpectedArrival() {
        return expectedArrival;
    }

    public void setExpectedArrival(LocalDateTime expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "GateAssignmentRequestDTO{" +
                "flightId=" + flightId +
                ", gateId=" + gateId +
                ", expectedArrival=" + expectedArrival +
                '}';
    }
}
