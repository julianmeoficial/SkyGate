package com.skygate.backend.model.entity;

import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gates")
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Gate number is required")
    @Column(name = "gate_number", nullable = false, unique = true, length = 10)
    private String gateNumber;

    @NotNull(message = "Gate type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "gate_type", nullable = false, length = 20)
    private GateType gateType;

    @NotNull(message = "Gate status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private GateStatus status;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "terminal", length = 10)
    private String terminal;

    @Column(name = "led_path", length = 255)
    private String ledPath;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "gate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments;

    public Gate() {
        this.status = GateStatus.FREE;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.assignments = new ArrayList<>();
    }

    public Gate(String gateNumber, GateType gateType, String terminal, String location) {
        this();
        this.gateNumber = gateNumber;
        this.gateType = gateType;
        this.terminal = terminal;
        this.location = location;
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
        this.updatedAt = LocalDateTime.now();
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
        this.updatedAt = LocalDateTime.now();
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

    public boolean isAvailable() {
        return this.status.isAvailable() && this.isActive;
    }

    public void occupy() {
        this.status = GateStatus.OCCUPIED;
        this.updatedAt = LocalDateTime.now();
    }

    public void free() {
        this.status = GateStatus.FREE;
        this.updatedAt = LocalDateTime.now();
    }

    public void assign() {
        this.status = GateStatus.ASSIGNED;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Gate{" +
                "id=" + id +
                ", gateNumber='" + gateNumber + '\'' +
                ", gateType=" + gateType +
                ", status=" + status +
                ", terminal='" + terminal + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
