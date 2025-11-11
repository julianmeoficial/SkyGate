package com.skygate.backend.model.dto.request;

import com.skygate.backend.model.enums.FlightStatus;
import jakarta.validation.constraints.NotNull;

public class FlightStatusUpdateRequestDTO {

    @NotNull(message = "Flight status is required")
    private FlightStatus status;

    private String notes;

    public FlightStatusUpdateRequestDTO() {
    }

    public FlightStatusUpdateRequestDTO(FlightStatus status) {
        this.status = status;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "FlightStatusUpdateRequestDTO{" +
                "status=" + status +
                ", notes='" + notes + '\'' +
                '}';
    }
}
