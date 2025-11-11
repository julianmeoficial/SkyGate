package com.skygate.backend.controller;

import com.skygate.backend.model.dto.request.GateAssignmentRequestDTO;
import com.skygate.backend.model.dto.response.ApiResponseDTO;
import com.skygate.backend.model.dto.response.AssignmentResponseDTO;
import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.service.assignment.AssignmentService;
import com.skygate.backend.util.Constants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.ApiConstants.ASSIGNMENT_ENDPOINT)
public class AssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<AssignmentResponseDTO>> createAssignment(
            @Valid @RequestBody GateAssignmentRequestDTO request) {
        logger.info("Creating assignment: Flight {} -> Gate {}", request.getFlightId(), request.getGateId());
        Assignment assignment = assignmentService.createAssignment(request.getFlightId(), request.getGateId());
        AssignmentResponseDTO response = AssignmentResponseDTO.fromEntity(assignment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Assignment created successfully", response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<AssignmentResponseDTO>>> getActiveAssignments() {
        logger.info("Fetching active assignments via /active endpoint");
        List<Assignment> assignments = assignmentService.getActiveAssignments();
        List<AssignmentResponseDTO> response = assignments.stream()
                .map(AssignmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AssignmentResponseDTO>> getAssignmentById(@PathVariable Long id) {
        logger.info("Fetching assignment by ID: {}", id);
        Assignment assignment = assignmentService.getAssignmentById(id);
        AssignmentResponseDTO response = AssignmentResponseDTO.fromEntity(assignment);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AssignmentResponseDTO>>> getAllAssignments(
            @RequestParam(required = false) Boolean activeOnly) {
        logger.info("Fetching all assignments (activeOnly: {})", activeOnly);
        List<Assignment> assignments = (activeOnly != null && activeOnly)
                ? assignmentService.getActiveAssignments()
                : assignmentService.getAllAssignments();
        List<AssignmentResponseDTO> response = assignments.stream()
                .map(AssignmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<ApiResponseDTO<AssignmentResponseDTO>> getActiveAssignmentByFlight(
            @PathVariable Long flightId) {
        logger.info("Fetching active assignment for flight ID: {}", flightId);
        Assignment assignment = assignmentService.getActiveAssignmentByFlightId(flightId)
                .orElseThrow(() -> new IllegalArgumentException("No active assignment found for flight"));
        AssignmentResponseDTO response = AssignmentResponseDTO.fromEntity(assignment);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteAssignment(@PathVariable Long id) {
        logger.info("Deleting assignment with ID: {}", id);
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Assignment deleted successfully", null));
    }
}
