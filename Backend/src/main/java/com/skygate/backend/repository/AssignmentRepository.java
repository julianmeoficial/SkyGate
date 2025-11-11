package com.skygate.backend.repository;

import com.skygate.backend.model.entity.Assignment;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Optional<Assignment> findByFlightAndIsActiveTrue(Flight flight);

    Optional<Assignment> findByGateAndIsActiveTrue(Gate gate);

    List<Assignment> findByFlight(Flight flight);

    List<Assignment> findByGate(Gate gate);

    List<Assignment> findByIsActiveTrue();

    List<Assignment> findByIsActiveFalse();

    @Query("SELECT a FROM Assignment a WHERE a.flight.id = :flightId AND a.isActive = true")
    Optional<Assignment> findActiveAssignmentByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT a FROM Assignment a WHERE a.gate.id = :gateId AND a.isActive = true")
    Optional<Assignment> findActiveAssignmentByGateId(@Param("gateId") Long gateId);

    @Query("SELECT a FROM Assignment a JOIN FETCH a.flight JOIN FETCH a.gate WHERE a.id = :assignmentId")
    Optional<Assignment> findByIdWithFlightAndGate(@Param("assignmentId") Long assignmentId);

    @Query("SELECT a FROM Assignment a JOIN FETCH a.flight f JOIN FETCH a.gate g WHERE a.isActive = true")
    List<Assignment> findAllActiveWithFlightAndGate();

    @Query("SELECT a FROM Assignment a WHERE a.assignedAt >= :startDate AND a.assignedAt <= :endDate")
    List<Assignment> findAssignmentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Assignment a WHERE a.gate.gateNumber = :gateNumber ORDER BY a.assignedAt DESC")
    List<Assignment> findAssignmentHistoryByGateNumber(@Param("gateNumber") String gateNumber);

    @Query("SELECT a FROM Assignment a WHERE a.flight.flightNumber = :flightNumber ORDER BY a.assignedAt DESC")
    List<Assignment> findAssignmentHistoryByFlightNumber(@Param("flightNumber") String flightNumber);

    @Modifying
    @Query("UPDATE Assignment a SET a.isActive = false, a.departureTime = CURRENT_TIMESTAMP, a.updatedAt = CURRENT_TIMESTAMP WHERE a.id = :assignmentId")
    int deactivateAssignment(@Param("assignmentId") Long assignmentId);

    @Modifying
    @Query("UPDATE Assignment a SET a.ledActivated = :ledStatus, a.updatedAt = CURRENT_TIMESTAMP WHERE a.id = :assignmentId")
    int updateLedStatus(@Param("assignmentId") Long assignmentId, @Param("ledStatus") Boolean ledStatus);

    @Modifying
    @Query("UPDATE Assignment a SET a.actualArrival = :arrivalTime, a.updatedAt = CURRENT_TIMESTAMP WHERE a.id = :assignmentId")
    int updateActualArrival(@Param("assignmentId") Long assignmentId, @Param("arrivalTime") LocalDateTime arrivalTime);

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.isActive = true")
    long countActiveAssignments();

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.gate.id = :gateId")
    long countAssignmentsByGateId(@Param("gateId") Long gateId);

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.ledActivated = true AND a.isActive = true")
    long countActiveAssignmentsWithLedsOn();

    @Query("SELECT a FROM Assignment a WHERE a.gate.terminal = :terminal AND a.isActive = true")
    List<Assignment> findActiveAssignmentsByTerminal(@Param("terminal") String terminal);

    boolean existsByFlightAndIsActiveTrue(Flight flight);

    boolean existsByGateAndIsActiveTrue(Gate gate);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Assignment a WHERE a.flight.id = :flightId AND a.gate.id = :gateId AND a.isActive = true")
    boolean existsActiveAssignmentByFlightAndGate(@Param("flightId") Long flightId, @Param("gateId") Long gateId);
}
