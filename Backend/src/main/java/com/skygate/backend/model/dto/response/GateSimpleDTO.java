package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;

public class GateSimpleDTO {

    private Long id;
    private String gateNumber;
    private GateType gateType;
    private GateStatus status;
    private String terminal;

    public GateSimpleDTO() {
    }

    public GateSimpleDTO(Gate gate) {
        this.id = gate.getId();
        this.gateNumber = gate.getGateNumber();
        this.gateType = gate.getGateType();
        this.status = gate.getStatus();
        this.terminal = gate.getTerminal();
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

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return "GateSimpleDTO{" +
                "id=" + id +
                ", gateNumber='" + gateNumber + '\'' +
                ", status=" + status +
                '}';
    }
}
