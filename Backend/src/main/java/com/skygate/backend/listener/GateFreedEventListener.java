package com.skygate.backend.listener;

import com.skygate.backend.event.GateFreedEvent;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import com.skygate.backend.repository.FlightRepository;
import com.skygate.backend.service.automata.WaitingFlightProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GateFreedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(GateFreedEventListener.class);

    private final FlightRepository flightRepository;
    private final WaitingFlightProcessor waitingFlightProcessor;

    public GateFreedEventListener(
            FlightRepository flightRepository,
            WaitingFlightProcessor waitingFlightProcessor) {
        this.flightRepository = flightRepository;
        this.waitingFlightProcessor = waitingFlightProcessor;
    }

    @EventListener
    @Async
    @Transactional
    public void handleGateFreed(GateFreedEvent event) {
        Gate freedGate = event.getGate();
        logger.info("Gate freed event received: {} - Reason: {}",
                freedGate.getGateNumber(), event.getReason());

        List<Flight> waitingFlights = flightRepository
                .findByAutomataStateAndStatus(AutomataState.S6, FlightStatus.WAITING);

        if (waitingFlights.isEmpty()) {
            logger.info("No waiting flights found. Gate {} will remain free.",
                    freedGate.getGateNumber());
            return;
        }

        logger.info("Found {} waiting flights. Attempting automatic reassignment.",
                waitingFlights.size());

        Flight compatibleFlight = findCompatibleWaitingFlight(waitingFlights, freedGate);

        if (compatibleFlight != null) {
            logger.info("Compatible flight found: {} for gate {}",
                    compatibleFlight.getFlightNumber(),
                    freedGate.getGateNumber());

            waitingFlightProcessor.retryGateAssignment(compatibleFlight);
        } else {
            logger.info("No compatible waiting flights found for gate type: {}",
                    freedGate.getGateType());
        }
    }

    private Flight findCompatibleWaitingFlight(List<Flight> waitingFlights, Gate gate) {
        AircraftType gateCompatibleType = AircraftType.fromGateType(gate.getGateType());

        return waitingFlights.stream()
                .filter(flight -> {
                    AircraftType flightType = flight.getAircraft().getAircraftType();
                    return flightType == gateCompatibleType ||
                            flightType.isCompatibleWith(gate.getGateType());
                })
                .findFirst()
                .orElse(null);
    }
}
