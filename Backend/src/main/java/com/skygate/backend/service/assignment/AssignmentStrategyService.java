package com.skygate.backend.service.assignment;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.GateType;
import com.skygate.backend.service.gate.GateAvailabilityService;
import com.skygate.backend.exception.NoAvailableGateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentStrategyService.class);

    private final GateAvailabilityService gateAvailabilityService;

    public AssignmentStrategyService(GateAvailabilityService gateAvailabilityService) {
        this.gateAvailabilityService = gateAvailabilityService;
    }

    public Gate findOptimalGate(Flight flight) {
        logger.info("Finding optimal gate for flight {}", flight.getFlightNumber());

        AircraftType aircraftType = flight.getAircraft().getAircraftType();

        Optional<Gate> optimalGate = gateAvailabilityService.findAvailableGate(aircraftType);

        if (optimalGate.isPresent()) {
            logger.info("Optimal gate found: {} for flight {}",
                    optimalGate.get().getGateNumber(), flight.getFlightNumber());
            return optimalGate.get();
        }

        throw new NoAvailableGateException(aircraftType, flight.getFlightNumber());
    }

    public Gate findOptimalGateWithPreference(Flight flight, String preferredTerminal) {
        logger.info("Finding optimal gate for flight {} with terminal preference: {}",
                flight.getFlightNumber(), preferredTerminal);

        AircraftType aircraftType = flight.getAircraft().getAircraftType();

        Gate optimalGate = gateAvailabilityService.findBestAvailableGate(aircraftType, preferredTerminal);

        logger.info("Optimal gate found: {} for flight {} in terminal {}",
                optimalGate.getGateNumber(), flight.getFlightNumber(), optimalGate.getTerminal());

        return optimalGate;
    }

    public boolean canAssignGate(Flight flight, Gate gate) {
        logger.debug("Checking if gate {} can be assigned to flight {}",
                gate.getGateNumber(), flight.getFlightNumber());

        if (!gate.isAvailable()) {
            logger.warn("Gate {} is not available", gate.getGateNumber());
            return false;
        }

        AircraftType aircraftType = flight.getAircraft().getAircraftType();
        GateType gateType = gate.getGateType();

        boolean compatible = aircraftType.isCompatibleWith(gateType);

        if (!compatible) {
            logger.warn("Aircraft type {} is not compatible with gate type {}",
                    aircraftType, gateType);
        }

        return compatible;
    }

    public AssignmentScore scoreGateForFlight(Flight flight, Gate gate) {
        logger.debug("Scoring gate {} for flight {}",
                gate.getGateNumber(), flight.getFlightNumber());

        int score = 0;
        StringBuilder reasoning = new StringBuilder();

        if (!gate.isAvailable()) {
            return new AssignmentScore(0, "Gate not available");
        }

        AircraftType aircraftType = flight.getAircraft().getAircraftType();
        GateType gateType = gate.getGateType();

        if (!aircraftType.isCompatibleWith(gateType)) {
            return new AssignmentScore(0, "Incompatible aircraft and gate types");
        }

        if (GateType.fromAircraftType(aircraftType) == gateType) {
            score += 100;
            reasoning.append("Perfect match. ");
        } else {
            score += 50;
            reasoning.append("Compatible but oversized gate. ");
        }

        if (flight.getDestination() != null && gate.getTerminal() != null) {
            score += 20;
            reasoning.append("Terminal available. ");
        }

        if (gate.getStatus().name().equals("FREE")) {
            score += 30;
            reasoning.append("Gate completely free. ");
        }

        return new AssignmentScore(score, reasoning.toString().trim());
    }

    public List<Gate> getAllCompatibleGates(Flight flight) {
        AircraftType aircraftType = flight.getAircraft().getAircraftType();
        return gateAvailabilityService.findAllAvailableGates().stream()
                .filter(gate -> aircraftType.isCompatibleWith(gate.getGateType()))
                .toList();
    }

    public static class AssignmentScore {
        private final int score;
        private final String reasoning;

        public AssignmentScore(int score, String reasoning) {
            this.score = score;
            this.reasoning = reasoning;
        }

        public int getScore() {
            return score;
        }

        public String getReasoning() {
            return reasoning;
        }

        @Override
        public String toString() {
            return "Score: " + score + " - " + reasoning;
        }
    }
}
