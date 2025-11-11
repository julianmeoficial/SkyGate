package com.skygate.backend.service.assignment;

import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.repository.AssignmentRepository;
import com.skygate.backend.repository.FlightRepository;
import com.skygate.backend.repository.GateRepository;
import com.skygate.backend.exception.AssignmentNotFoundException;
import com.skygate.backend.exception.FlightNotFoundException;
import com.skygate.backend.exception.GateNotFoundException;
import com.skygate.backend.exception.GateAlreadyOccupiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    private final AssignmentRepository assignmentRepository;
    private final FlightRepository flightRepository;
    private final GateRepository gateRepository;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            FlightRepository flightRepository,
            GateRepository gateRepository) {
        this.assignmentRepository = assignmentRepository;
        this.flightRepository = flightRepository;
        this.gateRepository = gateRepository;
    }

    @Transactional
    public Assignment createAssignment(Flight flight, Gate gate) {
        logger.info("Creating assignment for flight {} to gate {}",
                flight.getFlightNumber(), gate.getGateNumber());

        if (assignmentRepository.existsByFlightAndIsActiveTrue(flight)) {
            throw new IllegalStateException("Flight " + flight.getFlightNumber() + " already has an active assignment");
        }

        if (assignmentRepository.existsByGateAndIsActiveTrue(gate)) {
            throw new GateAlreadyOccupiedException(gate.getGateNumber());
        }

        Assignment assignment = new Assignment();
        assignment.setFlight(flight);
        assignment.setGate(gate);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setExpectedArrival(flight.getScheduledArrival());
        assignment.setIsActive(true);
        assignment.setLedActivated(false);
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());

        Assignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment created successfully: Flight {} -> Gate {}",
                flight.getFlightNumber(), gate.getGateNumber());

        return savedAssignment;
    }

    @Transactional
    public Assignment createAssignment(Long flightId, Long gateId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));

        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> new GateNotFoundException(gateId));

        return createAssignment(flight, gate);
    }

    @Transactional(readOnly = true)
    public Assignment getAssignmentById(Long assignmentId) {
        logger.debug("Fetching assignment by ID: {}", assignmentId);
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> getActiveAssignmentByFlight(Flight flight) {
        logger.debug("Fetching active assignment for flight: {}", flight.getFlightNumber());
        return assignmentRepository.findByFlightAndIsActiveTrue(flight);
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> getActiveAssignmentByFlightId(Long flightId) {
        logger.debug("Fetching active assignment for flight ID: {}", flightId);
        return assignmentRepository.findActiveAssignmentByFlightId(flightId);
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> getActiveAssignmentByGate(Gate gate) {
        logger.debug("Fetching active assignment for gate: {}", gate.getGateNumber());
        return assignmentRepository.findByGateAndIsActiveTrue(gate);
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> getActiveAssignmentByGateId(Long gateId) {
        logger.debug("Fetching active assignment for gate ID: {}", gateId);
        return assignmentRepository.findActiveAssignmentByGateId(gateId);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAllAssignments() {
        logger.debug("Fetching all assignments");
        return assignmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Assignment> getActiveAssignments() {
        logger.debug("Fetching all active assignments");
        return assignmentRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Assignment> getActiveAssignmentsWithDetails() {
        logger.debug("Fetching all active assignments with flight and gate details");
        return assignmentRepository.findAllActiveWithFlightAndGate();
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Fetching assignments between {} and {}", startDate, endDate);
        return assignmentRepository.findAssignmentsByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentHistoryByGate(String gateNumber) {
        logger.debug("Fetching assignment history for gate: {}", gateNumber);
        return assignmentRepository.findAssignmentHistoryByGateNumber(gateNumber);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentHistoryByFlight(String flightNumber) {
        logger.debug("Fetching assignment history for flight: {}", flightNumber);
        return assignmentRepository.findAssignmentHistoryByFlightNumber(flightNumber);
    }

    @Transactional
    public Assignment updateAssignment(Long assignmentId, Assignment assignmentDetails) {
        logger.info("Updating assignment with ID: {}", assignmentId);

        Assignment assignment = getAssignmentById(assignmentId);

        if (assignmentDetails.getExpectedArrival() != null) {
            assignment.setExpectedArrival(assignmentDetails.getExpectedArrival());
        }

        if (assignmentDetails.getNotes() != null) {
            assignment.setNotes(assignmentDetails.getNotes());
        }

        assignment.setUpdatedAt(LocalDateTime.now());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment updated successfully: ID {}", assignmentId);

        return updatedAssignment;
    }

    @Transactional
    public Assignment activateLeds(Long assignmentId) {
        logger.info("Activating LEDs for assignment ID: {}", assignmentId);

        Assignment assignment = getAssignmentById(assignmentId);
        assignment.setLedActivated(true);
        assignment.setUpdatedAt(LocalDateTime.now());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        logger.info("LEDs activated for assignment: Flight {} -> Gate {}",
                assignment.getFlight().getFlightNumber(),
                assignment.getGate().getGateNumber());

        return updatedAssignment;
    }

    @Transactional
    public Assignment deactivateLeds(Long assignmentId) {
        logger.info("Deactivating LEDs for assignment ID: {}", assignmentId);

        Assignment assignment = getAssignmentById(assignmentId);
        assignment.setLedActivated(false);
        assignment.setUpdatedAt(LocalDateTime.now());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        logger.info("LEDs deactivated for assignment ID: {}", assignmentId);

        return updatedAssignment;
    }

    @Transactional
    public Assignment recordArrival(Long assignmentId) {
        logger.info("Recording arrival for assignment ID: {}", assignmentId);

        Assignment assignment = getAssignmentById(assignmentId);
        assignment.setActualArrival(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        logger.info("Arrival recorded for assignment: Flight {} at Gate {}",
                assignment.getFlight().getFlightNumber(),
                assignment.getGate().getGateNumber());

        return updatedAssignment;
    }

    @Transactional
    public Assignment completeAssignment(Long assignmentId) {
        logger.info("Completing assignment ID: {}", assignmentId);

        Assignment assignment = getAssignmentById(assignmentId);
        assignment.setIsActive(false);
        assignment.setDepartureTime(LocalDateTime.now());
        assignment.setLedActivated(false);
        assignment.setUpdatedAt(LocalDateTime.now());

        Assignment completedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment completed: Flight {} departed from Gate {}",
                assignment.getFlight().getFlightNumber(),
                assignment.getGate().getGateNumber());

        return completedAssignment;
    }

    @Transactional
    public void deleteAssignment(Long assignmentId) {
        logger.info("Deleting assignment with ID: {}", assignmentId);

        Assignment assignment = getAssignmentById(assignmentId);

        if (assignment.getIsActive()) {
            throw new IllegalStateException("Cannot delete active assignment. Complete it first.");
        }

        assignmentRepository.delete(assignment);
        logger.info("Assignment deleted: ID {}", assignmentId);
    }

    @Transactional(readOnly = true)
    public long countActiveAssignments() {
        return assignmentRepository.countActiveAssignments();
    }

    @Transactional(readOnly = true)
    public long countAssignmentsByGate(Long gateId) {
        return assignmentRepository.countAssignmentsByGateId(gateId);
    }

    @Transactional(readOnly = true)
    public long countActiveAssignmentsWithLedsOn() {
        return assignmentRepository.countActiveAssignmentsWithLedsOn();
    }

    @Transactional(readOnly = true)
    public boolean hasActiveAssignment(Flight flight) {
        return assignmentRepository.existsByFlightAndIsActiveTrue(flight);
    }

    @Transactional(readOnly = true)
    public boolean isGateOccupied(Gate gate) {
        return assignmentRepository.existsByGateAndIsActiveTrue(gate);
    }

    @Transactional(readOnly = true)
    public Assignment getAssignmentWithDetails(Long assignmentId) {
        return assignmentRepository.findByIdWithFlightAndGate(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));
    }

    @Transactional(readOnly = true)
    public List<Assignment> getActiveAssignmentsByTerminal(String terminal) {
        logger.debug("Fetching active assignments for terminal: {}", terminal);
        return assignmentRepository.findActiveAssignmentsByTerminal(terminal);
    }
}
