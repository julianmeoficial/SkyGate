package com.skygate.backend.controller;

import com.skygate.backend.model.dto.request.GateRequestDTO;
import com.skygate.backend.model.dto.request.GateStatusUpdateRequestDTO;
import com.skygate.backend.model.dto.response.ApiResponseDTO;
import com.skygate.backend.model.dto.response.GateResponseDTO;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import com.skygate.backend.service.gate.GateService;
import com.skygate.backend.service.gate.GateAvailabilityService;
import com.skygate.backend.util.Constants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.ApiConstants.GATE_ENDPOINT)
public class GateController {

    private static final Logger logger = LoggerFactory.getLogger(GateController.class);

    private final GateService gateService;
    private final GateAvailabilityService gateAvailabilityService;

    public GateController(GateService gateService, GateAvailabilityService gateAvailabilityService) {
        this.gateService = gateService;
        this.gateAvailabilityService = gateAvailabilityService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> createGate(@Valid @RequestBody GateRequestDTO request) {
        logger.info("Creating new gate: {}", request.getGateNumber());

        Gate gate = new Gate();
        gate.setGateNumber(request.getGateNumber());
        gate.setGateType(request.getGateType());
        gate.setLocation(request.getLocation());
        gate.setTerminal(request.getTerminal());
        gate.setLedPath(request.getLedPath());

        Gate createdGate = gateService.createGate(gate);
        GateResponseDTO response = GateResponseDTO.fromEntity(createdGate);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Gate created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> getGateById(@PathVariable Long id) {
        logger.info("Fetching gate by ID: {}", id);

        Gate gate = gateService.getGateById(id);
        GateResponseDTO response = GateResponseDTO.fromEntity(gate);

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/number/{gateNumber}")
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> getGateByNumber(@PathVariable String gateNumber) {
        logger.info("Fetching gate by number: {}", gateNumber);

        Gate gate = gateService.getGateByNumber(gateNumber);
        GateResponseDTO response = GateResponseDTO.fromEntity(gate);

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<GateResponseDTO>>> getAllGates(
            @RequestParam(required = false) Boolean activeOnly) {
        logger.info("Fetching all gates (activeOnly: {})", activeOnly);

        List<Gate> gates = (activeOnly != null && activeOnly)
                ? gateService.getActiveGates()
                : gateService.getAllGates();

        List<GateResponseDTO> response = gates.stream()
                .map(GateResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<GateResponseDTO>>> getGatesByStatus(@PathVariable GateStatus status) {
        logger.info("Fetching gates by status: {}", status);

        List<Gate> gates = gateService.getGatesByStatus(status);
        List<GateResponseDTO> response = gates.stream()
                .map(GateResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/type/{gateType}")
    public ResponseEntity<ApiResponseDTO<List<GateResponseDTO>>> getGatesByType(@PathVariable GateType gateType) {
        logger.info("Fetching gates by type: {}", gateType);

        List<Gate> gates = gateService.getGatesByType(gateType);
        List<GateResponseDTO> response = gates.stream()
                .map(GateResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/terminal/{terminal}")
    public ResponseEntity<ApiResponseDTO<List<GateResponseDTO>>> getGatesByTerminal(@PathVariable String terminal) {
        logger.info("Fetching gates by terminal: {}", terminal);

        List<Gate> gates = gateService.getGatesByTerminal(terminal);
        List<GateResponseDTO> response = gates.stream()
                .map(GateResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponseDTO<List<GateResponseDTO>>> getAvailableGates() {
        logger.info("Fetching all available gates");

        List<Gate> gates = gateAvailabilityService.findAllAvailableGates();
        List<GateResponseDTO> response = gates.stream()
                .map(GateResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> updateGate(
            @PathVariable Long id,
            @Valid @RequestBody GateRequestDTO request) {
        logger.info("Updating gate with ID: {}", id);

        Gate gateDetails = new Gate();
        gateDetails.setGateNumber(request.getGateNumber());
        gateDetails.setGateType(request.getGateType());
        gateDetails.setLocation(request.getLocation());
        gateDetails.setTerminal(request.getTerminal());
        gateDetails.setLedPath(request.getLedPath());

        Gate updatedGate = gateService.updateGate(id, gateDetails);
        GateResponseDTO response = GateResponseDTO.fromEntity(updatedGate);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate updated successfully", response));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> updateGateStatus(
            @PathVariable Long id,
            @Valid @RequestBody GateStatusUpdateRequestDTO request) {
        logger.info("Updating gate status for ID: {} to {}", id, request.getStatus());

        Gate updatedGate = gateService.updateGateStatus(id, request.getStatus());
        GateResponseDTO response = GateResponseDTO.fromEntity(updatedGate);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate status updated successfully", response));
    }

    @PutMapping("/{id}/occupy")
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> occupyGate(@PathVariable Long id) {
        logger.info("Occupying gate with ID: {}", id);

        Gate occupiedGate = gateService.occupyGate(id);
        GateResponseDTO response = GateResponseDTO.fromEntity(occupiedGate);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate occupied successfully", response));
    }

    @PutMapping("/{id}/free")
    public ResponseEntity<ApiResponseDTO<GateResponseDTO>> freeGate(@PathVariable Long id) {
        logger.info("Freeing gate with ID: {}", id);

        Gate freedGate = gateService.freeGate(id);
        GateResponseDTO response = GateResponseDTO.fromEntity(freedGate);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate freed successfully", response));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<String>> deactivateGate(@PathVariable Long id) {
        logger.info("Deactivating gate with ID: {}", id);

        gateService.deactivateGate(id);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate deactivated successfully", null));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO<String>> activateGate(@PathVariable Long id) {
        logger.info("Activating gate with ID: {}", id);

        gateService.activateGate(id);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate activated successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteGate(@PathVariable Long id) {
        logger.info("Deleting gate with ID: {}", id);

        gateService.deleteGate(id);

        return ResponseEntity.ok(ApiResponseDTO.success("Gate deleted successfully", null));
    }

    @GetMapping("/stats/count")
    public ResponseEntity<ApiResponseDTO<Long>> countGatesByStatus(@RequestParam GateStatus status) {
        logger.info("Counting gates by status: {}", status);

        long count = gateService.countGatesByStatus(status);

        return ResponseEntity.ok(ApiResponseDTO.success(count));
    }
}
