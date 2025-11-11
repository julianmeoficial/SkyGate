package com.skygate.backend.websocket;

import com.skygate.backend.model.dto.response.AssignmentResponseDTO;
import com.skygate.backend.model.dto.response.AutomataTransitionResponseDTO;
import com.skygate.backend.model.dto.response.FlightResponseDTO;
import com.skygate.backend.model.dto.response.GateResponseDTO;
import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.service.automata.StateTransitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventPublisher.class);

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishGateUpdate(Gate gate) {
        logger.info("Publishing gate update via WebSocket: {}", gate.getGateNumber());
        GateResponseDTO response = GateResponseDTO.fromEntity(gate);
        messagingTemplate.convertAndSend("/topic/gates/update", response);
    }

    public void publishGateStatusChange(Gate gate) {
        logger.info("Publishing gate status change: {} -> {}", gate.getGateNumber(), gate.getStatus());

        Map<String, Object> event = new HashMap<>();
        event.put("type", "GATE_STATUS_CHANGE");
        event.put("gateId", gate.getId());
        event.put("gateNumber", gate.getGateNumber());
        event.put("status", gate.getStatus());
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/gates/status", event);
    }

    public void publishFlightUpdate(Flight flight) {
        logger.info("Publishing flight update via WebSocket: {}", flight.getFlightNumber());
        FlightResponseDTO response = FlightResponseDTO.fromEntity(flight);
        messagingTemplate.convertAndSend("/topic/flights/update", response);
    }

    public void publishFlightDetection(Flight flight) {
        logger.info("Publishing flight detection: {}", flight.getFlightNumber());

        Map<String, Object> event = new HashMap<>();
        event.put("type", "FLIGHT_DETECTED");
        event.put("flightId", flight.getId());
        event.put("flightNumber", flight.getFlightNumber());
        event.put("aircraftType", flight.getAircraft().getAircraftType());
        event.put("automataState", flight.getAutomataState());
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/flights/detection", event);
    }

    public void publishFlightStatusChange(Flight flight) {
        logger.info("Publishing flight status change: {} -> {}", flight.getFlightNumber(), flight.getStatus());

        Map<String, Object> event = new HashMap<>();
        event.put("type", "FLIGHT_STATUS_CHANGE");
        event.put("flightId", flight.getId());
        event.put("flightNumber", flight.getFlightNumber());
        event.put("status", flight.getStatus());
        event.put("automataState", flight.getAutomataState());
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/flights/status", event);
    }

    public void publishAssignmentCreated(Assignment assignment) {
        logger.info("Publishing assignment created: Flight {} -> Gate {}",
                assignment.getFlight().getFlightNumber(),
                assignment.getGate().getGateNumber());

        AssignmentResponseDTO response = AssignmentResponseDTO.fromEntity(assignment);
        messagingTemplate.convertAndSend("/topic/assignments/created", response);
    }

    public void publishAssignmentCompleted(Assignment assignment) {
        logger.info("Publishing assignment completed: {}", assignment.getId());

        Map<String, Object> event = new HashMap<>();
        event.put("type", "ASSIGNMENT_COMPLETED");
        event.put("assignmentId", assignment.getId());
        event.put("flightNumber", assignment.getFlight().getFlightNumber());
        event.put("gateNumber", assignment.getGate().getGateNumber());
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/assignments/completed", event);
    }

    public void publishAutomataTransition(StateTransitionService.TransitionResult result, Flight flight) {
        logger.info("Publishing automata transition: {} -> {}",
                result.getPreviousState(), result.getNewState());

        AutomataTransitionResponseDTO response = new AutomataTransitionResponseDTO(result);

        Map<String, Object> event = new HashMap<>();
        event.put("type", "AUTOMATA_TRANSITION");
        event.put("flightId", flight.getId());
        event.put("flightNumber", flight.getFlightNumber());
        event.put("previousState", result.getPreviousState());
        event.put("newState", result.getNewState());
        event.put("input", result.getInput());
        event.put("outputs", result.getOutputs());
        event.put("timestamp", result.getTimestamp());

        messagingTemplate.convertAndSend("/topic/automata/transitions", event);
    }

    public void publishLedStatusChange(Long gateId, String gateNumber, String color, boolean active) {
        logger.info("Publishing LED status change: Gate {} - {} - Active: {}", gateNumber, color, active);

        Map<String, Object> event = new HashMap<>();
        event.put("type", "LED_STATUS_CHANGE");
        event.put("gateId", gateId);
        event.put("gateNumber", gateNumber);
        event.put("color", color);
        event.put("active", active);
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/leds/status", event);
    }

    public void publishSystemAlert(String severity, String message, String details) {
        logger.warn("Publishing system alert: {} - {}", severity, message);

        Map<String, Object> alert = new HashMap<>();
        alert.put("type", "SYSTEM_ALERT");
        alert.put("severity", severity);
        alert.put("message", message);
        alert.put("details", details);
        alert.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/system/alerts", alert);
    }

    public void publishDashboardUpdate(Map<String, Object> stats) {
        logger.debug("Publishing dashboard update");

        Map<String, Object> event = new HashMap<>();
        event.put("type", "DASHBOARD_UPDATE");
        event.put("stats", stats);
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/dashboard/stats", event);
    }

    public void publishNoGateAvailable(Flight flight) {
        logger.warn("Publishing no gate available alert for flight: {}", flight.getFlightNumber());

        Map<String, Object> event = new HashMap<>();
        event.put("type", "NO_GATE_AVAILABLE");
        event.put("flightId", flight.getId());
        event.put("flightNumber", flight.getFlightNumber());
        event.put("aircraftType", flight.getAircraft().getAircraftType());
        event.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/system/alerts", event);
    }
}
