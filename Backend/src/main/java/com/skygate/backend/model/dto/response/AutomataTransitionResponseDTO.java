package com.skygate.backend.model.dto.response;

import com.skygate.backend.model.enums.AutomataInput;
import com.skygate.backend.model.enums.AutomataOutput;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.service.automata.StateTransitionService;
import java.util.List;

public class AutomataTransitionResponseDTO {

    private AutomataState previousState;
    private AutomataState newState;
    private AutomataInput input;
    private List<AutomataOutput> outputs;
    private Long timestamp;
    private String message;

    public AutomataTransitionResponseDTO() {
    }

    public AutomataTransitionResponseDTO(StateTransitionService.TransitionResult result) {
        this.previousState = result.getPreviousState();
        this.newState = result.getNewState();
        this.input = result.getInput();
        this.outputs = result.getOutputs();
        this.timestamp = result.getTimestamp();
        this.message = buildMessage(result);
    }

    private String buildMessage(StateTransitionService.TransitionResult result) {
        return String.format("Transition: %s -> %s with input %s",
                result.getPreviousState().getCode(),
                result.getNewState().getCode(),
                result.getInput().getCode());
    }

    public AutomataState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(AutomataState previousState) {
        this.previousState = previousState;
    }

    public AutomataState getNewState() {
        return newState;
    }

    public void setNewState(AutomataState newState) {
        this.newState = newState;
    }

    public AutomataInput getInput() {
        return input;
    }

    public void setInput(AutomataInput input) {
        this.input = input;
    }

    public List<AutomataOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<AutomataOutput> outputs) {
        this.outputs = outputs;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AutomataTransitionResponseDTO{" +
                "previousState=" + previousState +
                ", newState=" + newState +
                ", input=" + input +
                ", outputs=" + outputs +
                '}';
    }
}
