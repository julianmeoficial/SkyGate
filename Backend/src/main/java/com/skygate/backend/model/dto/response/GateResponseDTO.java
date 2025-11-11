package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import java.time.LocalDateTime;

public class GateResponseDTO {

    private Long id;
    private String gateNumber;
    private GateType gateType;
    private GateStatus status;
    private String location;
    private String terminal;
    private String ledPath;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GateResponseDTO() {
    }

    public GateResponseDTO(Gate gate) {
        this.id = gate.getId();
        this.gateNumber = gate.getGateNumber();
        this.gateType = gate.getGateType();
        this.status = gate.getStatus();
        this.location = gate.getLocation();
        this.terminal = gate.getTerminal();
        this.ledPath = gate.getLedPath();
        this.isActive = gate.getIsActive();
        this.createdAt = gate.getCreatedAt();
        this.updatedAt = gate.getUpdatedAt();
    }

    public static GateResponseDTO fromEntity(Gate gate) {
        return new GateResponseDTO(gate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public GateStatus getStatus() {
        return status;
    }

    public void setStatus(GateStatus status) {
        this.status = status;
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
        return "GateResponseDTO{" +
                "id=" + id +
                ", gateNumber='" + gateNumber + '\'' +
                ", gateType=" + gateType +
                ", status=" + status +
                ", terminal='" + terminal + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
