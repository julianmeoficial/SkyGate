package com.skygate.backend.model.dto.request;

import com.skygate.backend.model.enums.GateStatus;
import jakarta.validation.constraints.NotNull;

public class GateStatusUpdateRequestDTO {

    @NotNull(message = "Gate status is required")
    private GateStatus status;

    private String reason;

    public GateStatusUpdateRequestDTO() {
    }

    public GateStatusUpdateRequestDTO(GateStatus status) {
        this.status = status;
    }

    public GateStatus getStatus() {
        return status;
    }

    public void setStatus(GateStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "GateStatusUpdateRequestDTO{" +
                "status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
