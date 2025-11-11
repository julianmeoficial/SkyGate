package com.skygate.backend.repository;

import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {

    Optional<Gate> findByGateNumber(String gateNumber);

    List<Gate> findByStatus(GateStatus status);

    List<Gate> findByGateType(GateType gateType);

    List<Gate> findByIsActiveTrue();

    List<Gate> findByStatusAndIsActiveTrue(GateStatus status);

    List<Gate> findByGateTypeAndStatusAndIsActiveTrue(GateType gateType, GateStatus status);

    @Query("SELECT g FROM Gate g WHERE g.gateType = :gateType AND g.status = 'FREE' AND g.isActive = true")
    List<Gate> findAvailableGatesByType(@Param("gateType") GateType gateType);

    @Query("SELECT g FROM Gate g WHERE g.terminal = :terminal AND g.isActive = true")
    List<Gate> findByTerminal(@Param("terminal") String terminal);

    @Query("SELECT g FROM Gate g WHERE g.terminal = :terminal AND g.status = :status AND g.isActive = true")
    List<Gate> findByTerminalAndStatus(@Param("terminal") String terminal, @Param("status") GateStatus status);

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Gate g WHERE g.gateType = :gateType AND g.status = 'FREE' AND g.isActive = true")
    boolean existsAvailableGateByType(@Param("gateType") GateType gateType);

    @Query("SELECT COUNT(g) FROM Gate g WHERE g.status = :status AND g.isActive = true")
    long countByStatus(@Param("status") GateStatus status);

    @Query("SELECT COUNT(g) FROM Gate g WHERE g.gateType = :gateType AND g.status = 'FREE' AND g.isActive = true")
    long countAvailableByType(@Param("gateType") GateType gateType);

    @Modifying
    @Query("UPDATE Gate g SET g.status = :status, g.updatedAt = CURRENT_TIMESTAMP WHERE g.id = :gateId")
    int updateGateStatus(@Param("gateId") Long gateId, @Param("status") GateStatus status);

    @Modifying
    @Query("UPDATE Gate g SET g.isActive = :isActive, g.updatedAt = CURRENT_TIMESTAMP WHERE g.id = :gateId")
    int updateGateActiveStatus(@Param("gateId") Long gateId, @Param("isActive") Boolean isActive);

    @Query("SELECT g FROM Gate g WHERE g.status IN ('FREE', 'RESERVED') AND g.isActive = true ORDER BY g.gateType DESC")
    List<Gate> findAllAvailableGatesOrderByTypeDesc();

    boolean existsByGateNumber(String gateNumber);

    @Query("SELECT g FROM Gate g LEFT JOIN FETCH g.assignments WHERE g.id = :gateId")
    Optional<Gate> findByIdWithAssignments(@Param("gateId") Long gateId);
}
