package com.skygate.backend.controller;

import com.skygate.backend.model.dto.request.FlightDetectionRequestDTO;
import com.skygate.backend.model.dto.request.FlightRequestDTO;
import com.skygate.backend.model.dto.request.FlightStatusUpdateRequestDTO;
import com.skygate.backend.model.dto.response.ApiResponseDTO;
import com.skygate.backend.model.dto.response.FlightResponseDTO;
import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import com.skygate.backend.repository.AircraftRepository;
import com.skygate.backend.service.flight.FlightDetectionService;
import com.skygate.backend.service.flight.FlightService;
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
@RequestMapping(Constants.ApiConstants.FLIGHT_ENDPOINT)
public class FlightController {

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
    private final FlightService flightService;
    private final FlightDetectionService flightDetectionService;
    private final AircraftRepository aircraftRepository;

    public FlightController(
            FlightService flightService,
            FlightDetectionService flightDetectionService,
            AircraftRepository aircraftRepository) {
        this.flightService = flightService;
        this.flightDetectionService = flightDetectionService;
        this.aircraftRepository = aircraftRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FlightResponseDTO>> createFlight(@Valid @RequestBody FlightRequestDTO request) {
        logger.info("Creating new flight: {}", request.getFlightNumber());
        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found"));
        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setAircraft(aircraft);
        flight.setOrigin(request.getOrigin());
        flight.setDestination(request.getDestination());
        flight.setAirline(request.getAirline());
        flight.setScheduledArrival(request.getScheduledArrival());
        flight.setScheduledDeparture(request.getScheduledDeparture());
        Flight createdFlight = flightService.createFlight(flight);
        FlightResponseDTO response = FlightResponseDTO.fromEntity(createdFlight);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Flight created successfully", response));
    }

    @PostMapping("/detect")
    public ResponseEntity<ApiResponseDTO<FlightResponseDTO>> detectFlight(
            @Valid @RequestBody FlightDetectionRequestDTO request) {
        logger.info("Detecting flight: {}", request.getFlightNumber());
        Flight detectedFlight = flightDetectionService.detectFlightByAircraftType(
                request.getFlightNumber(),
                request.getAircraftType(),
                request.getOrigin(),
                request.getDestination(),
                request.getAirline() != null ? request.getAirline() : "Unknown Airline"
        );
        FlightResponseDTO response = FlightResponseDTO.fromEntity(detectedFlight);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(true, "Flight detected successfully", response)
        );
    }

    @PostMapping("/simulate")
    public ResponseEntity<ApiResponseDTO<FlightResponseDTO>> simulateFlight() {
        logger.info("Simulating random flight detection");
        Flight simulatedFlight = flightDetectionService.simulateFlightDetection();
        FlightResponseDTO response = FlightResponseDTO.fromEntity(simulatedFlight);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Flight simulated successfully", response));
    }

    @PostMapping("/{id}/arrival")
    public ResponseEntity<ApiResponseDTO<Object>> simulateArrival(@PathVariable Long id) {
        logger.info("Simulating arrival for flight ID: {}", id);
        flightDetectionService.simulateFlightArrival(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Flight arrival simulated successfully", null));
    }

    @PostMapping("/{id}/departure")
    public ResponseEntity<ApiResponseDTO<Object>> simulateDeparture(@PathVariable Long id) {
        logger.info("Simulating departure for flight ID: {}", id);
        flightDetectionService.simulateFlightDeparture(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Flight departure simulated successfully", null));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<FlightResponseDTO>>> getActiveFlights() {
        logger.info("Fetching active flights via /active endpoint");
        List<Flight> flights = flightService.getActiveFlights();
        List<FlightResponseDTO> response = flights.stream()
                .map(FlightResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FlightResponseDTO>> getFlightById(@PathVariable Long id) {
        logger.info("Fetching flight by ID: {}", id);
        Flight flight = flightService.getFlightById(id);
        FlightResponseDTO response = FlightResponseDTO.fromEntity(flight);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FlightResponseDTO>>> getAllFlights(
            @RequestParam(required = false) Boolean activeOnly) {
        logger.info("Fetching all flights (activeOnly: {})", activeOnly);
        List<Flight> flights = (activeOnly != null && activeOnly)
                ? flightService.getActiveFlights()
                : flightService.getAllFlights();
        List<FlightResponseDTO> response = flights.stream()
                .map(FlightResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<ApiResponseDTO<FlightResponseDTO>> getFlightByNumber(@PathVariable String flightNumber) {
        logger.info("Fetching flight by number: {}", flightNumber);
        Flight flight = flightService.getFlightByNumber(flightNumber);
        FlightResponseDTO response = FlightResponseDTO.fromEntity(flight);
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<FlightResponseDTO>>> getFlightsByStatus(@PathVariable FlightStatus status) {
        logger.info("Fetching flights by status: {}", status);
        List<Flight> flights = flightService.getFlightsByStatus(status);
        List<FlightResponseDTO> response = flights.stream()
                .map(FlightResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @GetMapping("/automata-state/{state}")
    public ResponseEntity<ApiResponseDTO<List<FlightResponseDTO>>> getFlightsByAutomataState(
            @PathVariable AutomataState state) {
        logger.info("Fetching flights by automata state: {}", state);
        List<Flight> flights = flightService.getFlightsByAutomataState(state);
        List<FlightResponseDTO> response = flights.stream()
                .map(FlightResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FlightResponseDTO>> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightRequestDTO request) {
        logger.info("Updating flight with ID: {}", id);
        Flight flightDetails = new Flight();
        flightDetails.setOrigin(request.getOrigin());
        flightDetails.setDestination(request.getDestination());
        flightDetails.setAirline(request.getAirline());
        flightDetails.setScheduledArrival(request.getScheduledArrival());
        flightDetails.setScheduledDeparture(request.getScheduledDeparture());
        Flight updatedFlight = flightService.updateFlight(id, flightDetails);
        FlightResponseDTO response = FlightResponseDTO.fromEntity(updatedFlight);
        return ResponseEntity.ok(ApiResponseDTO.success("Flight updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteFlight(@PathVariable Long id) {
        logger.info("Deleting flight with ID: {}", id);
        flightService.deleteFlight(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Flight deleted successfully", null));
    }
}
