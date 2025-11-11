package com.skygate.backend.service.flight;

import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import com.skygate.backend.repository.AircraftRepository;
import com.skygate.backend.repository.FlightRepository;
import com.skygate.backend.exception.AircraftNotFoundException;
import com.skygate.backend.exception.FlightNotFoundException;
import com.skygate.backend.exception.InvalidFlightDataException;
import com.skygate.backend.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;

    public FlightService(FlightRepository flightRepository, AircraftRepository aircraftRepository) {
        this.flightRepository = flightRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Transactional
    public Flight createFlight(Flight flight) {
        logger.info("Creating new flight: {}", flight.getFlightNumber());

        validateFlightData(flight);

        if (flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new IllegalArgumentException("Flight with number " + flight.getFlightNumber() + " already exists");
        }

        if (flight.getAircraft() != null && flight.getAircraft().getId() != null) {
            Aircraft aircraft = aircraftRepository.findById(flight.getAircraft().getId())
                    .orElseThrow(() -> new AircraftNotFoundException(flight.getAircraft().getId()));
            flight.setAircraft(aircraft);
        }

        flight.setStatus(FlightStatus.DETECTED);
        flight.setAutomataState(AutomataState.S0);
        flight.setDetectedAt(LocalDateTime.now());
        flight.setCreatedAt(LocalDateTime.now());
        flight.setUpdatedAt(LocalDateTime.now());

        Flight savedFlight = flightRepository.save(flight);
        logger.info("Flight created successfully: {}", savedFlight.getFlightNumber());

        return savedFlight;
    }

    @Transactional(readOnly = true)
    public Flight getFlightById(Long flightId) {
        logger.debug("Fetching flight by ID: {}", flightId);
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    @Transactional(readOnly = true)
    public Flight getFlightByNumber(String flightNumber) {
        logger.debug("Fetching flight by number: {}", flightNumber);
        return flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException(flightNumber, true));
    }

    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        logger.debug("Fetching all flights");
        return flightRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Flight> getActiveFlights() {
        logger.debug("Fetching all active flights");
        return flightRepository.findAllActiveFlights();
    }

    @Transactional(readOnly = true)
    public List<Flight> getFlightsByStatus(FlightStatus status) {
        logger.debug("Fetching flights by status: {}", status);
        return flightRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAutomataState(AutomataState state) {
        logger.debug("Fetching flights by automata state: {}", state);
        return flightRepository.findByAutomataState(state);
    }

    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAirline(String airline) {
        logger.debug("Fetching flights by airline: {}", airline);
        return flightRepository.findByAirline(airline);
    }

    @Transactional(readOnly = true)
    public List<Flight> getFlightsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Fetching flights between {} and {}", startDate, endDate);
        return flightRepository.findFlightsByDetectionDateRange(startDate, endDate);
    }

    @Transactional
    public Flight updateFlight(Long flightId, Flight flightDetails) {
        logger.info("Updating flight with ID: {}", flightId);

        Flight flight = getFlightById(flightId);

        if (flightDetails.getOrigin() != null) {
            flight.setOrigin(flightDetails.getOrigin());
        }

        if (flightDetails.getDestination() != null) {
            flight.setDestination(flightDetails.getDestination());
        }

        if (flightDetails.getAirline() != null) {
            flight.setAirline(flightDetails.getAirline());
        }

        if (flightDetails.getScheduledArrival() != null) {
            flight.setScheduledArrival(flightDetails.getScheduledArrival());
        }

        if (flightDetails.getScheduledDeparture() != null) {
            flight.setScheduledDeparture(flightDetails.getScheduledDeparture());
        }

        flight.setUpdatedAt(LocalDateTime.now());

        Flight updatedFlight = flightRepository.save(flight);
        logger.info("Flight updated successfully: {}", updatedFlight.getFlightNumber());

        return updatedFlight;
    }

    @Transactional
    public Flight updateFlightStatus(Long flightId, FlightStatus newStatus) {
        logger.info("Updating flight status for ID: {} to {}", flightId, newStatus);

        Flight flight = getFlightById(flightId);
        flight.setStatus(newStatus);
        flight.setUpdatedAt(LocalDateTime.now());

        Flight updatedFlight = flightRepository.save(flight);
        logger.info("Flight status updated: {} -> {}", flight.getFlightNumber(), newStatus);

        return updatedFlight;
    }

    @Transactional
    public Flight updateAutomataState(Long flightId, AutomataState newState) {
        logger.info("Updating automata state for flight ID: {} to {}", flightId, newState);

        Flight flight = getFlightById(flightId);
        flight.setAutomataState(newState);
        flight.setUpdatedAt(LocalDateTime.now());

        Flight updatedFlight = flightRepository.save(flight);
        logger.info("Automata state updated: {} -> {}", flight.getFlightNumber(), newState);

        return updatedFlight;
    }

    @Transactional
    public Flight recordActualArrival(Long flightId) {
        logger.info("Recording actual arrival for flight ID: {}", flightId);

        Flight flight = getFlightById(flightId);
        flight.setActualArrival(LocalDateTime.now());
        flight.setUpdatedAt(LocalDateTime.now());

        return flightRepository.save(flight);
    }

    @Transactional
    public Flight recordActualDeparture(Long flightId) {
        logger.info("Recording actual departure for flight ID: {}", flightId);

        Flight flight = getFlightById(flightId);
        flight.setActualDeparture(LocalDateTime.now());
        flight.setUpdatedAt(LocalDateTime.now());

        return flightRepository.save(flight);
    }

    @Transactional
    public void deleteFlight(Long flightId) {
        logger.info("Deleting flight with ID: {}", flightId);

        Flight flight = getFlightById(flightId);

        if (flight.getStatus().isActive()) {
            throw new IllegalStateException("Cannot delete active flight: " + flight.getFlightNumber());
        }

        flightRepository.delete(flight);
        logger.info("Flight deleted: {}", flight.getFlightNumber());
    }

    @Transactional(readOnly = true)
    public long countFlightsByStatus(FlightStatus status) {
        return flightRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countFlightsByAutomataState(AutomataState state) {
        return flightRepository.countByAutomataState(state);
    }

    @Transactional(readOnly = true)
    public Flight getFlightWithAircraft(Long flightId) {
        return flightRepository.findByIdWithAircraft(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    @Transactional(readOnly = true)
    public Flight getFlightWithAssignments(Long flightId) {
        return flightRepository.findByIdWithAssignments(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    private void validateFlightData(Flight flight) {
        if (flight == null) {
            throw new InvalidFlightDataException("Flight data cannot be null");
        }

        if (!ValidationUtil.isValidFlightNumber(flight.getFlightNumber())) {
            throw new InvalidFlightDataException("Invalid flight number format", "flightNumber");
        }

        if (flight.getAircraft() == null) {
            throw new InvalidFlightDataException("Aircraft is required", "aircraft");
        }

        if (flight.getScheduledArrival() != null && flight.getScheduledDeparture() != null) {
            if (!ValidationUtil.isValidDateRange(flight.getScheduledArrival(), flight.getScheduledDeparture())) {
                throw new InvalidFlightDataException("Scheduled departure must be after scheduled arrival", "scheduledDeparture");
            }
        }
    }
}
