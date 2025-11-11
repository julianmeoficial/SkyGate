package com.skygate.backend.websocket;

import com.skygate.backend.model.dto.response.AssignmentResponseDTO;
import com.skygate.backend.model.dto.response.FlightResponseDTO;
import com.skygate.backend.model.dto.response.GateResponseDTO;
import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.service.assignment.AssignmentService;
import com.skygate.backend.service.flight.FlightService;
import com.skygate.backend.service.gate.GateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    private final GateService gateService;
    private final FlightService flightService;
    private final AssignmentService assignmentService;

    public WebSocketController(
            GateService gateService,
            FlightService flightService,
            AssignmentService assignmentService) {
        this.gateService = gateService;
        this.flightService = flightService;
        this.assignmentService = assignmentService;
    }

    @MessageMapping("/gates/subscribe")
    @SendTo("/topic/gates")
    public List<GateResponseDTO> subscribeToGates() {
        logger.info("Client subscribed to gates updates");
        List<Gate> gates = gateService.getAllGates();
        return gates.stream()
                .map(GateResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @MessageMapping("/flights/subscribe")
    @SendTo("/topic/flights")
    public List<FlightResponseDTO> subscribeToFlights() {
        logger.info("Client subscribed to flights updates");
        List<Flight> flights = flightService.getActiveFlights();
        return flights.stream()
                .map(FlightResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @MessageMapping("/assignments/subscribe")
    @SendTo("/topic/assignments")
    public List<AssignmentResponseDTO> subscribeToAssignments() {
        logger.info("Client subscribed to assignments updates");
        List<Assignment> assignments = assignmentService.getActiveAssignments();
        return assignments.stream()
                .map(AssignmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @MessageMapping("/dashboard/subscribe")
    @SendToUser("/queue/dashboard")
    public String subscribeToDashboard() {
        logger.info("Client subscribed to dashboard updates");
        return "Subscribed to real-time dashboard updates";
    }
}
