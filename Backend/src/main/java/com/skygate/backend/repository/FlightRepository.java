package com.skygate.backend.repository;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AutomataState;
import com.skygate.backend.model.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByStatus(FlightStatus status);

    List<Flight> findByAutomataState(AutomataState automataState);

    List<Flight> findByAirline(String airline);

    List<Flight> findByOrigin(String origin);

    List<Flight> findByDestination(String destination);

    @Query("SELECT f FROM Flight f WHERE f.status IN :statuses")
    List<Flight> findByStatusIn(@Param("statuses") List<FlightStatus> statuses);

    @Query("SELECT f FROM Flight f WHERE f.automataState IN ('S1', 'S2', 'S3', 'S4', 'S5')")
    List<Flight> findAllActiveFlights();

    @Query("SELECT f FROM Flight f WHERE f.automataState = 'S0' OR f.automataState = 'S6'")
    List<Flight> findAllInactiveFlights();

    @Query("SELECT f FROM Flight f WHERE f.detectedAt >= :startDate AND f.detectedAt <= :endDate")
    List<Flight> findFlightsByDetectionDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f WHERE f.scheduledArrival >= :startDate AND f.scheduledArrival <= :endDate")
    List<Flight> findFlightsByScheduledArrivalRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Flight f JOIN FETCH f.aircraft WHERE f.id = :flightId")
    Optional<Flight> findByIdWithAircraft(@Param("flightId") Long flightId);

    @Query("SELECT f FROM Flight f LEFT JOIN FETCH f.assignments WHERE f.id = :flightId")
    Optional<Flight> findByIdWithAssignments(@Param("flightId") Long flightId);

    @Query("SELECT f FROM Flight f JOIN FETCH f.aircraft a WHERE f.flightNumber = :flightNumber")
    Optional<Flight> findByFlightNumberWithAircraft(@Param("flightNumber") String flightNumber);

    @Modifying
    @Query("UPDATE Flight f SET f.status = :status, f.updatedAt = CURRENT_TIMESTAMP WHERE f.id = :flightId")
    int updateFlightStatus(@Param("flightId") Long flightId, @Param("status") FlightStatus status);

    @Modifying
    @Query("UPDATE Flight f SET f.automataState = :state, f.updatedAt = CURRENT_TIMESTAMP WHERE f.id = :flightId")
    int updateAutomataState(@Param("flightId") Long flightId, @Param("state") AutomataState state);

    @Modifying
    @Query("UPDATE Flight f SET f.actualArrival = :arrivalTime, f.updatedAt = CURRENT_TIMESTAMP WHERE f.id = :flightId")
    int updateActualArrival(@Param("flightId") Long flightId, @Param("arrivalTime") LocalDateTime arrivalTime);

    @Modifying
    @Query("UPDATE Flight f SET f.actualDeparture = :departureTime, f.updatedAt = CURRENT_TIMESTAMP WHERE f.id = :flightId")
    int updateActualDeparture(@Param("flightId") Long flightId, @Param("departureTime") LocalDateTime departureTime);

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.status = :status")
    long countByStatus(@Param("status") FlightStatus status);

    @Query("SELECT COUNT(f) FROM Flight f WHERE f.automataState = :state")
    long countByAutomataState(@Param("state") AutomataState state);

    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE f.aircraft.aircraftType = :aircraftType AND f.status IN ('DETECTED', 'CONFIRMED', 'GATE_ASSIGNED')")
    List<Flight> findActiveFlightsByAircraftType(@Param("aircraftType") String aircraftType);

    @Query("SELECT f FROM Flight f WHERE f.automataState = :state AND f.status = :status ORDER BY f.detectedAt ASC")
    List<Flight> findByAutomataStateAndStatus(
            @Param("state") AutomataState state,
            @Param("status") FlightStatus status
    );
}
