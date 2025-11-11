package com.skygate.backend.model.dto.request;

import com.skygate.backend.model.enums.AutomataInput;
import jakarta.validation.constraints.NotNull;

public class AutomataInputRequestDTO {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Automata input is required")
    private AutomataInput input;

    private String context;

    public AutomataInputRequestDTO() {
    }

    public AutomataInputRequestDTO(Long flightId, AutomataInput input) {
        this.flightId = flightId;
        this.input = input;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public AutomataInput getInput() {
        return input;
    }

    public void setInput(AutomataInput input) {
        this.input = input;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "AutomataInputRequestDTO{" +
                "flightId=" + flightId +
                ", input=" + input +
                ", context='" + context + '\'' +
                '}';
    }
}
