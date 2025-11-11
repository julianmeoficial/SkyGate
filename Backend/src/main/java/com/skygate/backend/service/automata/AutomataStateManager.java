package com.skygate.backend.service.automata;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.AutomataInput;
import com.skygate.backend.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AutomataStateManager {

    private final Map<Long, AutomataState> flightStateMap;
    private final Map<Long, Long> stateTransitionTimestamps;

    public AutomataStateManager() {
        this.flightStateMap = new ConcurrentHashMap<>();
        this.stateTransitionTimestamps = new ConcurrentHashMap<>();
    }

    public AutomataState getCurrentState(Flight flight) {
        if (flight == null) {
            return AutomataState.S0;
        }
        return flightStateMap.getOrDefault(flight.getId(), flight.getAutomataState());
    }

    public AutomataState getCurrentState(Long flightId) {
        if (flightId == null) {
            return AutomataState.S0;
        }
        return flightStateMap.getOrDefault(flightId, AutomataState.S0);
    }

    public void setState(Flight flight, AutomataState newState) {
        if (flight == null || newState == null) {
            throw new IllegalArgumentException("Flight and state cannot be null");
        }
        flightStateMap.put(flight.getId(), newState);
        stateTransitionTimestamps.put(flight.getId(), System.currentTimeMillis());
    }

    public void setState(Long flightId, AutomataState newState) {
        if (flightId == null || newState == null) {
            throw new IllegalArgumentException("Flight ID and state cannot be null");
        }
        flightStateMap.put(flightId, newState);
        stateTransitionTimestamps.put(flightId, System.currentTimeMillis());
    }

    // Permite I3 e I4 en S1/S2/S3
    public boolean canTransition(AutomataState currentState, AutomataInput input) {
        if (currentState == null || input == null) {
            return false;
        }

        switch (currentState) {
            case S0:
                return input == AutomataInput.I1;

            case S1:
            case S2:
            case S3:
                // Ahora acepta I2, I3, I4 y OTHER
                return input == AutomataInput.I2
                        || input == AutomataInput.I3
                        || input == AutomataInput.I4
                        || input == AutomataInput.OTHER;

            case S4:
                return input == AutomataInput.I5 || input == AutomataInput.OTHER;

            case S5:
                return input == AutomataInput.I6 || input == AutomataInput.OTHER;

            case S6:
                return input == AutomataInput.I1
                        || input == AutomataInput.I3  //Permite reasignación cuando gate se libera
                        || input == AutomataInput.OTHER;

            default:
                return false;
        }
    }

    public void validateTransition(AutomataState currentState, AutomataInput input, AutomataState nextState) {
        if (!canTransition(currentState, input)) {
            throw new InvalidStateTransitionException(currentState, input);
        }

        AutomataState expectedNextState = getExpectedNextState(currentState, input);
        if (expectedNextState != nextState && nextState != currentState) {
            throw new InvalidStateTransitionException(currentState, input, nextState);
        }
    }

    // Maneja I3 e I4 correctamente
    private AutomataState getExpectedNextState(AutomataState currentState, AutomataInput input) {
        switch (currentState) {
            case S0:
                if (input == AutomataInput.I1) {
                    return null; // Depende del tipo de aeronave
                }
                return AutomataState.S0;

            case S1:
            case S2:
            case S3:
                if (input == AutomataInput.I2) {
                    return currentState; // Se mantiene en el mismo estado después de confirmación
                }
                if (input == AutomataInput.I3) {
                    return AutomataState.S4; // AGREGADO: Gate disponible
                }
                if (input == AutomataInput.I4) {
                    return AutomataState.S6; // AGREGADO: Sin gates
                }
                return currentState;

            case S4:
                if (input == AutomataInput.I5) {
                    return AutomataState.S5;
                }
                return AutomataState.S4;

            case S5:
                if (input == AutomataInput.I6) {
                    return AutomataState.S0;
                }
                return AutomataState.S5;

            case S6:
                if (input == AutomataInput.I3) {
                    return AutomataState.S4;  // Cuando gate se libera, va a S4
                }
                return AutomataState.S6;

            default:
                return currentState;
        }
    }

    public void removeFlight(Long flightId) {
        if (flightId != null) {
            flightStateMap.remove(flightId);
            stateTransitionTimestamps.remove(flightId);
        }
    }

    public void clear() {
        flightStateMap.clear();
        stateTransitionTimestamps.clear();
    }

    public long getTimeSinceLastTransition(Long flightId) {
        if (flightId == null || !stateTransitionTimestamps.containsKey(flightId)) {
            return 0;
        }
        return System.currentTimeMillis() - stateTransitionTimestamps.get(flightId);
    }

    public Map<Long, AutomataState> getAllFlightStates() {
        return new ConcurrentHashMap<>(flightStateMap);
    }

    public int getActiveFlightsCount() {
        return flightStateMap.size();
    }
}
