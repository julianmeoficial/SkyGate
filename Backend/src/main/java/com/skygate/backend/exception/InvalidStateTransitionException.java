package com.skygate.backend.exception;

import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.AutomataInput;

public class InvalidStateTransitionException extends RuntimeException {

    private AutomataState currentState;
    private AutomataInput input;
    private AutomataState attemptedState;

    public InvalidStateTransitionException(String message) {
        super(message);
    }

    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStateTransitionException(AutomataState currentState, AutomataInput input) {
        super(String.format("Invalid transition: Cannot process input %s in state %s",
                input.getCode(), currentState.getCode()));
        this.currentState = currentState;
        this.input = input;
    }

    public InvalidStateTransitionException(AutomataState currentState, AutomataInput input, AutomataState attemptedState) {
        super(String.format("Invalid transition: Cannot transition from %s to %s with input %s",
                currentState.getCode(), attemptedState.getCode(), input.getCode()));
        this.currentState = currentState;
        this.input = input;
        this.attemptedState = attemptedState;
    }

    public AutomataState getCurrentState() {
        return currentState;
    }

    public AutomataInput getInput() {
        return input;
    }

    public AutomataState getAttemptedState() {
        return attemptedState;
    }
}
