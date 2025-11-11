package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.enums.AircraftType;
import java.time.LocalDateTime;

public class AircraftResponseDTO {

    private Long id;
    private String model;
    private AircraftType aircraftType;
    private String manufacturer;
    private Double wingspan;
    private Double length;
    private Integer maxPassengers;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AircraftResponseDTO() {
    }

    public AircraftResponseDTO(Aircraft aircraft) {
        this.id = aircraft.getId();
        this.model = aircraft.getModel();
        this.aircraftType = aircraft.getAircraftType();
        this.manufacturer = aircraft.getManufacturer();
        this.wingspan = aircraft.getWingspan();
        this.length = aircraft.getLength();
        this.maxPassengers = aircraft.getMaxPassengers();
        this.description = aircraft.getDescription();
        this.createdAt = aircraft.getCreatedAt();
        this.updatedAt = aircraft.getUpdatedAt();
    }

    public static AircraftResponseDTO fromEntity(Aircraft aircraft) {
        return new AircraftResponseDTO(aircraft);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getWingspan() {
        return wingspan;
    }

    public void setWingspan(Double wingspan) {
        this.wingspan = wingspan;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(Integer maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "AircraftResponseDTO{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", aircraftType=" + aircraftType +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
