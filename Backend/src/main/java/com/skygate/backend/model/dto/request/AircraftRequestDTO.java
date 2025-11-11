package com.skygate.backend.model.dto.request;

import com.skygate.backend.model.enums.AircraftType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AircraftRequestDTO {

    @NotBlank(message = "Aircraft model is required")
    @Size(min = 2, max = 50, message = "Model must be between 2 and 50 characters")
    private String model;

    @NotNull(message = "Aircraft type is required")
    private AircraftType aircraftType;

    @Size(max = 50, message = "Manufacturer must not exceed 50 characters")
    private String manufacturer;

    @Positive(message = "Wingspan must be positive")
    private Double wingspan;

    @Positive(message = "Length must be positive")
    private Double length;

    @Positive(message = "Max passengers must be positive")
    private Integer maxPassengers;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    public AircraftRequestDTO() {
    }

    public AircraftRequestDTO(String model, AircraftType aircraftType, String manufacturer) {
        this.model = model;
        this.aircraftType = aircraftType;
        this.manufacturer = manufacturer;
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

    @Override
    public String toString() {
        return "AircraftRequestDTO{" +
                "model='" + model + '\'' +
                ", aircraftType=" + aircraftType +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
