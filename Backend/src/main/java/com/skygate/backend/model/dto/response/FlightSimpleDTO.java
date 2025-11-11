package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;

public class FlightSimpleDTO {
    private Long id;
    private String flightNumber;
    private FlightStatus status;
    private AutomataState automataState;
    private String origin;
    private String destination;
    private String aircraftType;

    public FlightSimpleDTO() {
    }

    public FlightSimpleDTO(Flight flight) {
        this.id = flight.getId();
        this.flightNumber = flight.getFlightNumber();
        this.status = flight.getStatus();
        this.automataState = flight.getAutomataState();
        this.origin = flight.getOrigin();
        this.destination = flight.getDestination();

        if (flight.getAircraft() != null && flight.getAircraft().getAircraftType() != null) {
            this.aircraftType = flight.getAircraft().getAircraftType().name();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public AutomataState getAutomataState() {
        return automataState;
    }

    public void setAutomataState(AutomataState automataState) {
        this.automataState = automataState;
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

    // Getter y setter para aircraftType
    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    @Override
    public String toString() {
        return "FlightSimpleDTO{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", status=" + status +
                ", automataState=" + automataState +
                ", aircraftType='" + aircraftType + '\'' +
                '}';
    }
}
