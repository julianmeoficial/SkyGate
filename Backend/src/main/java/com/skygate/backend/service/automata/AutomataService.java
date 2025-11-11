package com.skygate.backend.service.automata;

import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.AutomataInput;
import com.skygate.backend.model.enums.AutomataOutput;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.repository.FlightRepository;
import com.skygate.backend.repository.AssignmentRepository;
import com.skygate.backend.service.gate.GateAvailabilityService;
import com.skygate.backend.service.assignment.AssignmentService;
import com.skygate.backend.service.hardware.HardwareService;
import com.skygate.backend.exception.FlightNotFoundException;
import com.skygate.backend.exception.NoAvailableGateException;
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
public class AutomataService {

    private static final Logger logger = LoggerFactory.getLogger(AutomataService.class);

    private final StateTransitionService transitionService;
    private final AutomataStateManager stateManager;
    private final FlightRepository flightRepository;
    private final AssignmentRepository assignmentRepository;
    private final GateAvailabilityService gateAvailabilityService;
    private final AssignmentService assignmentService;
    private final HardwareService hardwareService;
    private final SimpMessagingTemplate messagingTemplate;  // AGREGADO

    public AutomataService(
            StateTransitionService transitionService,
            AutomataStateManager stateManager,
            FlightRepository flightRepository,
            AssignmentRepository assignmentRepository,
            GateAvailabilityService gateAvailabilityService,
            @Lazy AssignmentService assignmentService,
            HardwareService hardwareService,
            SimpMessagingTemplate messagingTemplate) {  // AGREGADO
        this.transitionService = transitionService;
        this.stateManager = stateManager;
        this.flightRepository = flightRepository;
        this.assignmentRepository = assignmentRepository;
        this.gateAvailabilityService = gateAvailabilityService;
        this.assignmentService = assignmentService;
        this.hardwareService = hardwareService;
        this.messagingTemplate = messagingTemplate;  // AGREGADO
    }

    @Transactional
    public StateTransitionService.TransitionResult processFlightDetection(Flight flight) {
        logger.info("Processing flight detection for flight {}", flight.getFlightNumber());

        AircraftType aircraftType = flight.getAircraft().getAircraftType();
        StateTransitionService.TransitionResult result = transitionService.processInput(
                flight, AutomataInput.I1, aircraftType);

        flight.setAutomataState(result.getNewState());
        flight.setStatus(FlightStatus.DETECTED);
        flight.setDetectedAt(LocalDateTime.now());
        flightRepository.save(flight);

        // AGREGADO: Notificar transición por WebSocket
        notifyTransition(flight, result, "FLIGHT_DETECTED");

        executeOutputs(result.getOutputs(), flight, null);

        processAircraftTypeConfirmation(flight);

        return result;
    }

    @Transactional
    public StateTransitionService.TransitionResult processAircraftTypeConfirmation(Flight flight) {
        logger.info("Processing aircraft type confirmation for flight {}", flight.getFlightNumber());

        // PASO 1: Procesar I2 - Confirmación de tipo de aeronave
        StateTransitionService.TransitionResult confirmationResult = transitionService.processInput(
                flight, AutomataInput.I2, null);
        
        logger.info("Aircraft type confirmed for flight {} in state {}", 
                flight.getFlightNumber(), confirmationResult.getNewState().getCode());

        // PASO 2: Buscar gate disponible
        Optional<Gate> availableGate = gateAvailabilityService.findAvailableGate(
                flight.getAircraft().getAircraftType());

        // PASO 3: Determinar input según disponibilidad (I3 o I4)
        AutomataInput availabilityInput;
        if (availableGate.isPresent()) {
            availabilityInput = AutomataInput.I3; // Gate disponible
            logger.info("Gate available for flight {}, processing I3", flight.getFlightNumber());
        } else {
            availabilityInput = AutomataInput.I4; // No hay gates disponibles
            logger.info("No gate available for flight {}, processing I4", flight.getFlightNumber());
        }

        // PASO 4: Procesar transición de disponibilidad (I3 o I4)
        StateTransitionService.TransitionResult result = transitionService.processInput(
                flight, availabilityInput, null);

        flight.setAutomataState(result.getNewState());

        // PASO 5: Ejecutar acciones según el estado resultante
        if (result.getNewState() == AutomataState.S4) {
            flight.setStatus(FlightStatus.GATE_ASSIGNED);
            Gate assignedGate = availableGate.get();
            assignedGate.setStatus(GateStatus.ASSIGNED);

            Assignment assignment = assignmentService.createAssignment(flight, assignedGate);

            // Notificar transición por WebSocket
            notifyTransition(flight, result, "GATE_ASSIGNED: " + assignedGate.getGateNumber());

            executeOutputs(result.getOutputs(), flight, assignedGate);
        } else if (result.getNewState() == AutomataState.S6) {
            flight.setStatus(FlightStatus.WAITING);

            // Notificar transición por WebSocket
            notifyTransition(flight, result, "NO_GATE_AVAILABLE");

            executeOutputs(result.getOutputs(), flight, null);
        }

        flightRepository.save(flight);

        return result;
    }

    @Transactional
    public StateTransitionService.TransitionResult processAircraftArrival(Flight flight) {
        logger.info("Processing aircraft arrival for flight {}", flight.getFlightNumber());

        Optional<Assignment> assignmentOpt = assignmentRepository.findActiveAssignmentByFlightId(flight.getId());
        if (!assignmentOpt.isPresent()) {
            throw new IllegalStateException("No active assignment found for flight " + flight.getFlightNumber());
        }

        Assignment assignment = assignmentOpt.get();
        Gate gate = assignment.getGate();

        StateTransitionService.TransitionResult result = transitionService.processInput(
                flight, AutomataInput.I5, null);

        flight.setAutomataState(result.getNewState());
        flight.setStatus(FlightStatus.PARKED);
        flight.setActualArrival(LocalDateTime.now());

        gate.setStatus(GateStatus.OCCUPIED);
        assignment.setActualArrival(LocalDateTime.now());

        flightRepository.save(flight);
        assignmentRepository.save(assignment);

        // AGREGADO: Notificar transición por WebSocket
        notifyTransition(flight, result, "AIRCRAFT_ARRIVED");

        executeOutputs(result.getOutputs(), flight, gate);

        return result;
    }

    @Transactional
    public StateTransitionService.TransitionResult processAircraftDeparture(Flight flight) {
        logger.info("Processing aircraft departure for flight {}", flight.getFlightNumber());

        Optional<Assignment> assignmentOpt = assignmentRepository.findActiveAssignmentByFlightId(flight.getId());
        if (!assignmentOpt.isPresent()) {
            throw new IllegalStateException("No active assignment found for flight " + flight.getFlightNumber());
        }

        Assignment assignment = assignmentOpt.get();
        Gate gate = assignment.getGate();

        StateTransitionService.TransitionResult result = transitionService.processInput(
                flight, AutomataInput.I6, null);

        flight.setAutomataState(result.getNewState());
        flight.setStatus(FlightStatus.DEPARTED);
        flight.setActualDeparture(LocalDateTime.now());

        gate.setStatus(GateStatus.FREE);
        assignment.setIsActive(false);
        assignment.setDepartureTime(LocalDateTime.now());

        flightRepository.save(flight);
        assignmentRepository.save(assignment);

        // AGREGADO: Notificar transición por WebSocket
        notifyTransition(flight, result, "AIRCRAFT_DEPARTED");

        executeOutputs(result.getOutputs(), flight, gate);

        stateManager.removeFlight(flight.getId());

        return result;
    }

    // MÉTODO AGREGADO: Notificar transición por WebSocket
    private void notifyTransition(Flight flight, StateTransitionService.TransitionResult result, String event) {
        Map<String, Object> transition = new HashMap<>();
        transition.put("flightId", flight.getId());
        transition.put("flightNumber", flight.getFlightNumber());
        transition.put("fromState", result.getPreviousState().getCode());
        transition.put("toState", result.getNewState().getCode());
        transition.put("event", event);
        transition.put("timestamp", LocalDateTime.now());
        transition.put("input", result.getInput().getCode());

        messagingTemplate.convertAndSend("/topic/automata/transitions", transition);
        logger.info("WebSocket notification sent: {} -> {} ({})",
                result.getPreviousState().getCode(),
                result.getNewState().getCode(),
                flight.getFlightNumber());
    }

    private void executeOutputs(java.util.List<AutomataOutput> outputs, Flight flight, Gate gate) {
        for (AutomataOutput output : outputs) {
            logger.info("Executing output {} for flight {}", output.getCode(), flight.getFlightNumber());

            switch (output) {
                case O1:
                    if (gate != null) {
                        hardwareService.activateLeds(gate.getId(), gate.getGateNumber(), "GREEN");
                    }
                    break;

                case O2:
                    if (gate != null) {
                        hardwareService.activateLeds(gate.getId(), gate.getGateNumber(), "GREEN");
                        hardwareService.sendGateAssignmentNotification(
                                gate.getId(),
                                gate.getGateNumber(),
                                flight.getId(),
                                flight.getFlightNumber());
                    }
                    break;

                case O3:
                    if (gate != null) {
                        hardwareService.activateLeds(gate.getId(), gate.getGateNumber(), "RED");
                    }
                    break;

                case O4:
                    hardwareService.updateDisplayScreen(
                            "WAITING",
                            flight.getFlightNumber(),
                            "WAITING_FOR_GATE");
                    break;

                case O5:
                    logger.info("Database updated for flight {}", flight.getFlightNumber());
                    break;

                default:
                    logger.warn("Unknown output: {}", output);
                    break;
            }
        }
    }

    public AutomataState getCurrentState(Flight flight) {
        return stateManager.getCurrentState(flight);
    }

    public AutomataState getCurrentState(Long flightId) {
        return stateManager.getCurrentState(flightId);
    }

    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    public Flight getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException(flightNumber, true));
    }
}
