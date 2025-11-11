package com.skygate.backend.controller;

import com.skygate.backend.model.dto.response.ApiResponseDTO;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.service.assignment.AssignmentService;
import com.skygate.backend.service.flight.FlightService;
import com.skygate.backend.service.gate.GateService;
import com.skygate.backend.service.hardware.LedControlService;
import com.skygate.backend.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(Constants.ApiConstants.API_BASE_PATH + "/monitoring")
public class MonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);

    private final GateService gateService;
    private final FlightService flightService;
    private final AssignmentService assignmentService;
    private final LedControlService ledControlService;

    public MonitoringController(
            GateService gateService,
            FlightService flightService,
            AssignmentService assignmentService,
            LedControlService ledControlService) {
        this.gateService = gateService;
        this.flightService = flightService;
        this.assignmentService = assignmentService;
        this.ledControlService = ledControlService;
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getDashboardStats() {
        logger.info("Fetching dashboard statistics");

        Map<String, Object> stats = new HashMap<>();

        Map<String, Long> gateStats = new HashMap<>();
        gateStats.put("total", (long) gateService.getAllGates().size());
        gateStats.put("free", gateService.countGatesByStatus(GateStatus.FREE));
        gateStats.put("occupied", gateService.countGatesByStatus(GateStatus.OCCUPIED));
        gateStats.put("assigned", gateService.countGatesByStatus(GateStatus.ASSIGNED));
        gateStats.put("maintenance", gateService.countGatesByStatus(GateStatus.MAINTENANCE));

        Map<String, Long> flightStats = new HashMap<>();
        flightStats.put("total", (long) flightService.getAllFlights().size());
        flightStats.put("active", (long) flightService.getActiveFlights().size());
        flightStats.put("detected", flightService.countFlightsByStatus(FlightStatus.DETECTED));
        flightStats.put("gateAssigned", flightService.countFlightsByStatus(FlightStatus.GATE_ASSIGNED));
        flightStats.put("parked", flightService.countFlightsByStatus(FlightStatus.PARKED));
        flightStats.put("departed", flightService.countFlightsByStatus(FlightStatus.DEPARTED));

        Map<String, Long> automataStats = new HashMap<>();
        automataStats.put("s0", flightService.countFlightsByAutomataState(AutomataState.S0));
        automataStats.put("s1", flightService.countFlightsByAutomataState(AutomataState.S1));
        automataStats.put("s2", flightService.countFlightsByAutomataState(AutomataState.S2));
        automataStats.put("s3", flightService.countFlightsByAutomataState(AutomataState.S3));
        automataStats.put("s4", flightService.countFlightsByAutomataState(AutomataState.S4));
        automataStats.put("s5", flightService.countFlightsByAutomataState(AutomataState.S5));
        automataStats.put("s6", flightService.countFlightsByAutomataState(AutomataState.S6));

        Map<String, Long> assignmentStats = new HashMap<>();
        assignmentStats.put("active", assignmentService.countActiveAssignments());
        assignmentStats.put("ledsActivated", assignmentService.countActiveAssignmentsWithLedsOn());

        stats.put("gates", gateStats);
        stats.put("flights", flightStats);
        stats.put("automataStates", automataStats);
        stats.put("assignments", assignmentStats);

        return ResponseEntity.ok(ApiResponseDTO.success(stats));
    }

    @GetMapping("/led-status")
    public ResponseEntity<ApiResponseDTO<Map<Long, LedControlService.LedState>>> getLedStatus() {
        logger.info("Fetching LED status for all gates");

        Map<Long, LedControlService.LedState> ledStates = ledControlService.getAllLedStates();

        return ResponseEntity.ok(ApiResponseDTO.success(ledStates));
    }

    @GetMapping("/system-health")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getSystemHealth() {
        logger.info("Checking system health");

        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("gatesOnline", gateService.getActiveGates().size());
        health.put("activeFlights", flightService.getActiveFlights().size());
        health.put("activeAssignments", assignmentService.countActiveAssignments());

        return ResponseEntity.ok(ApiResponseDTO.success(health));
    }
}
