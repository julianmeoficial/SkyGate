package com.skygate.backend.service.automata;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.AutomataInput;
import com.skygate.backend.model.enums.AutomataOutput;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.exception.InvalidStateTransitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class StateTransitionService {

    private static final Logger logger = LoggerFactory.getLogger(StateTransitionService.class);

    private final AutomataStateManager stateManager;

    public StateTransitionService(AutomataStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public TransitionResult processInput(Flight flight, AutomataInput input, Object context) {
        AutomataState currentState = stateManager.getCurrentState(flight);

        logger.info("Processing input {} for flight {} in state {}",
                input.getCode(), flight.getFlightNumber(), currentState.getCode());

        stateManager.validateTransition(currentState, input, currentState);

        AutomataState nextState = determineNextState(currentState, input, context);
        List<AutomataOutput> outputs = determineOutputs(currentState, nextState, input);

        stateManager.setState(flight, nextState);

        logger.info("Transitioned flight {} from {} to {} with outputs: {}",
                flight.getFlightNumber(), currentState.getCode(), nextState.getCode(), outputs);

        return new TransitionResult(currentState, nextState, input, outputs);
    }

    private AutomataState determineNextState(AutomataState currentState, AutomataInput input, Object context) {
        switch (currentState) {
            case S0:
                return handleS0Transition(input, context);

            case S1:
            case S2:
            case S3:
                return handleDetectionStateTransition(currentState, input, context);

            case S4:
                return handleS4Transition(input);

            case S5:
                return handleS5Transition(input);

            case S6:
                return handleS6Transition(input);

            default:
                throw new InvalidStateTransitionException("Unknown state: " + currentState);
        }
    }

    private AutomataState handleS0Transition(AutomataInput input, Object context) {
        if (input == AutomataInput.I1) {
            if (context instanceof AircraftType) {
                AircraftType type = (AircraftType) context;
                return AutomataState.getDetectionState(type);
            }
            throw new IllegalArgumentException("Context must be AircraftType for I1 input");
        }
        return AutomataState.S0;
    }

    private AutomataState handleDetectionStateTransition(AutomataState currentState, AutomataInput input, Object context) {
        // I2: Solo confirma el tipo de aeronave, no cambia el estado
        if (input == AutomataInput.I2) {
            logger.info("Aircraft type confirmed, maintaining state {}", currentState.getCode());
            return currentState; // Mantiene S1, S2 o S3
        }
        
        // I3: Gate disponible encontrado → Transición a S4
        if (input == AutomataInput.I3) {
            logger.info("Gate available, transitioning from {} to S4", currentState.getCode());
            return AutomataState.S4;
        }
        
        // I4: No hay gates disponibles → Transición a S6
        if (input == AutomataInput.I4) {
            logger.info("No gates available, transitioning from {} to S6", currentState.getCode());
            return AutomataState.S6;
        }
        
        return currentState;
    }

    private AutomataState handleS4Transition(AutomataInput input) {
        if (input == AutomataInput.I5) {
            return AutomataState.S5;
        }
        return AutomataState.S4;
    }

    private AutomataState handleS5Transition(AutomataInput input) {
        if (input == AutomataInput.I6) {
            return AutomataState.S0;
        }
        return AutomataState.S5;
    }

    private AutomataState handleS6Transition(AutomataInput input) {
        if (input == AutomataInput.I1) {
            return AutomataState.S0;  // Reset del sistema
        }
        if (input == AutomataInput.I3) {
            return AutomataState.S4;  // Gate se liberó, asignar vuelo
        }
        return AutomataState.S6;  // Se mantiene en espera
    }

    private List<AutomataOutput> determineOutputs(AutomataState currentState, AutomataState nextState, AutomataInput input) {
        List<AutomataOutput> outputs = new ArrayList<>();

        // S0 -> S1/S2/S3 (I1: Detección inicial)
        if (currentState == AutomataState.S0 && (nextState == AutomataState.S1 || nextState == AutomataState.S2 || nextState == AutomataState.S3)) {
            outputs.add(AutomataOutput.O5); // Actualizar BD
        }

        // S1/S2/S3 -> S4 (I3: Gate disponible)
        if ((currentState == AutomataState.S1 || currentState == AutomataState.S2 || currentState == AutomataState.S3)
                && nextState == AutomataState.S4
                && input == AutomataInput.I3) {
            outputs.add(AutomataOutput.O1); // LEDs ruta
            outputs.add(AutomataOutput.O2); // LED verde gate
            outputs.add(AutomataOutput.O5); // Actualizar BD
        }

        // S1/S2/S3 -> S6 (I4: Sin gates disponibles)
        if ((currentState == AutomataState.S1 || currentState == AutomataState.S2 || currentState == AutomataState.S3)
                && nextState == AutomataState.S6
                && input == AutomataInput.I4) {
            outputs.add(AutomataOutput.O4); // Display "ESPERAR"
        }

        // S6 -> S4 (I3: Gate se liberó para vuelo en espera)
        if (currentState == AutomataState.S6
                && nextState == AutomataState.S4
                && input == AutomataInput.I3) {
            outputs.add(AutomataOutput.O1); // LEDs ruta
            outputs.add(AutomataOutput.O2); // LED verde gate
            outputs.add(AutomataOutput.O5); // Actualizar BD
        }

        // S4 -> S5 (I5: Aeronave llegó)
        if (currentState == AutomataState.S4 && nextState == AutomataState.S5) {
            outputs.add(AutomataOutput.O3); // LED rojo ocupado
            outputs.add(AutomataOutput.O5); // Actualizar BD
        }

        // S5 -> S0 (I6: Aeronave partió)
        if (currentState == AutomataState.S5 && nextState == AutomataState.S0) {
            outputs.add(AutomataOutput.O5); // Actualizar BD
        }

        return outputs;
    }

    public boolean isValidTransition(AutomataState from, AutomataInput input, AutomataState to) {
        try {
            stateManager.validateTransition(from, input, to);
            return true;
        } catch (InvalidStateTransitionException e) {
            return false;
        }
    }

    public static class TransitionResult {
        private final AutomataState previousState;
        private final AutomataState newState;
        private final AutomataInput input;
        private final List<AutomataOutput> outputs;
        private final long timestamp;

        public TransitionResult(AutomataState previousState, AutomataState newState, AutomataInput input, List<AutomataOutput> outputs) {
            this.previousState = previousState;
            this.newState = newState;
            this.input = input;
            this.outputs = outputs;
            this.timestamp = System.currentTimeMillis();
        }

        public AutomataState getPreviousState() {
            return previousState;
        }

        public AutomataState getNewState() {
            return newState;
        }

        public AutomataInput getInput() {
            return input;
        }

        public List<AutomataOutput> getOutputs() {
            return outputs;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean hasOutput(AutomataOutput output) {
            return outputs.contains(output);
        }
    }
}
