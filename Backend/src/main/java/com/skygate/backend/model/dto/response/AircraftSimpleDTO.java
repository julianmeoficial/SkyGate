package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.enums.AircraftType;

public class AircraftSimpleDTO {

    private Long id;
    private String model;
    private AircraftType aircraftType;
    private String manufacturer;

    public AircraftSimpleDTO() {
    }

    public AircraftSimpleDTO(Aircraft aircraft) {
        this.id = aircraft.getId();
        this.model = aircraft.getModel();
        this.aircraftType = aircraft.getAircraftType();
        this.manufacturer = aircraft.getManufacturer();
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

    @Override
    public String toString() {
        return "AircraftSimpleDTO{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", aircraftType=" + aircraftType +
                '}';
    }
}
