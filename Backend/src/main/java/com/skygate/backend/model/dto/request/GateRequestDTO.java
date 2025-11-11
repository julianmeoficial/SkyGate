package com.skygate.backend.model.dto.request;

import com.skygate.backend.model.enums.GateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GateRequestDTO {

    @NotBlank(message = "Gate number is required")
    @Size(min = 2, max = 10, message = "Gate number must be between 2 and 10 characters")
    private String gateNumber;

    @NotNull(message = "Gate type is required")
    private GateType gateType;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @Size(max = 10, message = "Terminal must not exceed 10 characters")
    private String terminal;

    @Size(max = 255, message = "LED path must not exceed 255 characters")
    private String ledPath;

    private Boolean isActive;

    public GateRequestDTO() {
    }

    public GateRequestDTO(String gateNumber, GateType gateType, String terminal) {
        this.gateNumber = gateNumber;
        this.gateType = gateType;
        this.terminal = terminal;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public GateType getGateType() {
        return gateType;
    }

    public void setGateType(GateType gateType) {
        this.gateType = gateType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getLedPath() {
        return ledPath;
    }

    public void setLedPath(String ledPath) {
        this.ledPath = ledPath;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "GateRequestDTO{" +
                "gateNumber='" + gateNumber + '\'' +
                ", gateType=" + gateType +
                ", terminal='" + terminal + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
