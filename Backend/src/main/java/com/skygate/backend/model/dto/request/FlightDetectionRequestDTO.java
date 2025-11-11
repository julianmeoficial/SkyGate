package com.skygate.backend.model.dto.request;

import com.skygate.backend.model.enums.AircraftType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FlightDetectionRequestDTO {

    @NotBlank(message = "Flight number is required")
    @Size(min = 2, max = 10, message = "Flight number must be between 2 and 10 characters")
    private String flightNumber;

    @Size(max = 50, message = "Aircraft model must not exceed 50 characters")
    private String aircraftModel;

    @NotNull(message = "Aircraft type is required")
    private AircraftType aircraftType;

    @Size(max = 100, message = "Origin must not exceed 100 characters")
    private String origin;

    @Size(max = 100, message = "Destination must not exceed 100 characters")
    private String destination;

    @Size(max = 50, message = "Airline must not exceed 50 characters")
    private String airline;

    public FlightDetectionRequestDTO() {
    }

    public FlightDetectionRequestDTO(String flightNumber, String aircraftModel, String origin, String destination) {
        this.flightNumber = flightNumber;
        this.aircraftModel = aircraftModel;
        this.origin = origin;
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    @Override
    public String toString() {
        return "FlightDetectionRequestDTO{" +
                "flightNumber='" + flightNumber + '\'' +
                ", aircraftModel='" + aircraftModel + '\'' +
                ", aircraftType=" + aircraftType +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", airline='" + airline + '\'' +
                '}';
    }
}
