package com.skygate.backend.service.automata;

import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AutomataInput;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.repository.FlightRepository;
import com.skygate.backend.service.assignment.AssignmentService;
import com.skygate.backend.service.gate.GateAvailabilityService;
import com.skygate.backend.service.gate.GateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WaitingFlightProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WaitingFlightProcessor.class);

    private final StateTransitionService transitionService;
    private final FlightRepository flightRepository;
    private final GateAvailabilityService gateAvailabilityService;
    private final GateService gateService;
    private final AssignmentService assignmentService;
    private final SimpMessagingTemplate messagingTemplate;

    public WaitingFlightProcessor(
            StateTransitionService transitionService,
            FlightRepository flightRepository,
            GateAvailabilityService gateAvailabilityService,
            GateService gateService,
            @Lazy AssignmentService assignmentService,
            SimpMessagingTemplate messagingTemplate) {
        this.transitionService = transitionService;
        this.flightRepository = flightRepository;
        this.gateAvailabilityService = gateAvailabilityService;
        this.gateService = gateService;
        this.assignmentService = assignmentService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public boolean retryGateAssignment(Flight flight) {
        logger.info("Retrying gate assignment for waiting flight: {}", flight.getFlightNumber());

        if (flight.getAutomataState() != AutomataState.S6) {
            logger.warn("Flight {} is not in waiting state (S6). Current state: {}",
                    flight.getFlightNumber(), flight.getAutomataState());
            return false;
        }

        Optional<Gate> availableGate = gateAvailabilityService
                .findAvailableGate(flight.getAircraft().getAircraftType());

        if (!availableGate.isPresent()) {
            logger.info("Still no available gate for flight {}. Remaining in S6.",
                    flight.getFlightNumber());
            return false;
        }

        Gate assignedGate = availableGate.get();
        logger.info("Gate {} now available for waiting flight {}",
                assignedGate.getGateNumber(), flight.getFlightNumber());

        StateTransitionService.TransitionResult result = transitionService
                .processInput(flight, AutomataInput.I3, null);

        flight.setAutomataState(AutomataState.S4);
        flight.setStatus(FlightStatus.GATE_ASSIGNED);
        flightRepository.save(flight);

        assignedGate.setStatus(GateStatus.ASSIGNED);
        gateService.updateGateStatus(assignedGate.getId(), GateStatus.ASSIGNED);

        Assignment assignment = assignmentService.createAssignment(flight, assignedGate);

        notifyGateReassignment(flight, assignedGate);

        logger.info("Successfully reassigned flight {} from S6 to S4 with gate {}",
                flight.getFlightNumber(), assignedGate.getGateNumber());

        return true;
    }

    private void notifyGateReassignment(Flight flight, Gate gate) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("flightId", flight.getId());
        notification.put("flightNumber", flight.getFlightNumber());
        notification.put("fromState", "S6");
        notification.put("toState", "S4");
        notification.put("gateNumber", gate.getGateNumber());
        notification.put("event", "WAITING_FLIGHT_REASSIGNED");
        notification.put("timestamp", LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/automata/transitions", notification);

        logger.info("WebSocket notification sent: Flight {} reassigned from S6 to S4 with gate {}",
                flight.getFlightNumber(), gate.getGateNumber());
    }
}
