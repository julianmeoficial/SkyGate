package com.skygate.backend.service.gate;

import com.skygate.backend.event.GateFreedEvent;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import com.skygate.backend.repository.GateRepository;
import com.skygate.backend.exception.GateNotFoundException;
import com.skygate.backend.exception.GateAlreadyOccupiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GateService {

    private static final Logger logger = LoggerFactory.getLogger(GateService.class);

    private final GateRepository gateRepository;
    private final ApplicationEventPublisher eventPublisher;

    public GateService(GateRepository gateRepository, ApplicationEventPublisher eventPublisher) {
        this.gateRepository = gateRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Gate createGate(Gate gate) {
        logger.info("Creating new gate: {}", gate.getGateNumber());

        if (gateRepository.existsByGateNumber(gate.getGateNumber())) {
            throw new IllegalArgumentException("Gate with number " + gate.getGateNumber() + " already exists");
        }

        gate.setStatus(GateStatus.FREE);
        gate.setIsActive(true);
        gate.setCreatedAt(LocalDateTime.now());
        gate.setUpdatedAt(LocalDateTime.now());

        Gate savedGate = gateRepository.save(gate);
        logger.info("Gate created successfully: {}", savedGate.getGateNumber());

        return savedGate;
    }

    @Transactional(readOnly = true)
    public Gate getGateById(Long gateId) {
        logger.debug("Fetching gate by ID: {}", gateId);
        return gateRepository.findById(gateId)
                .orElseThrow(() -> new GateNotFoundException(gateId));
    }

    @Transactional(readOnly = true)
    public Gate getGateByNumber(String gateNumber) {
        logger.debug("Fetching gate by number: {}", gateNumber);
        return gateRepository.findByGateNumber(gateNumber)
                .orElseThrow(() -> new GateNotFoundException(gateNumber, true));
    }

    @Transactional(readOnly = true)
    public List<Gate> getAllGates() {
        logger.debug("Fetching all gates");
        return gateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Gate> getActiveGates() {
        logger.debug("Fetching all active gates");
        return gateRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Gate> getGatesByStatus(GateStatus status) {
        logger.debug("Fetching gates by status: {}", status);
        return gateRepository.findByStatusAndIsActiveTrue(status);
    }

    @Transactional(readOnly = true)
    public List<Gate> getGatesByType(GateType gateType) {
        logger.debug("Fetching gates by type: {}", gateType);
        return gateRepository.findByGateType(gateType);
    }

    @Transactional(readOnly = true)
    public List<Gate> getGatesByTerminal(String terminal) {
        logger.debug("Fetching gates by terminal: {}", terminal);
        return gateRepository.findByTerminal(terminal);
    }

    @Transactional
    public Gate updateGate(Long gateId, Gate gateDetails) {
        logger.info("Updating gate with ID: {}", gateId);

        Gate gate = getGateById(gateId);

        if (gateDetails.getGateNumber() != null && !gateDetails.getGateNumber().equals(gate.getGateNumber())) {
            if (gateRepository.existsByGateNumber(gateDetails.getGateNumber())) {
                throw new IllegalArgumentException("Gate number " + gateDetails.getGateNumber() + " is already in use");
            }
            gate.setGateNumber(gateDetails.getGateNumber());
        }

        if (gateDetails.getGateType() != null) {
            gate.setGateType(gateDetails.getGateType());
        }

        if (gateDetails.getLocation() != null) {
            gate.setLocation(gateDetails.getLocation());
        }

        if (gateDetails.getTerminal() != null) {
            gate.setTerminal(gateDetails.getTerminal());
        }

        if (gateDetails.getLedPath() != null) {
            gate.setLedPath(gateDetails.getLedPath());
        }

        gate.setUpdatedAt(LocalDateTime.now());

        Gate updatedGate = gateRepository.save(gate);
        logger.info("Gate updated successfully: {}", updatedGate.getGateNumber());

        return updatedGate;
    }

    @Transactional
    public Gate updateGateStatus(Long gateId, GateStatus newStatus) {
        logger.info("Updating gate status for ID: {} to {}", gateId, newStatus);

        Gate gate = getGateById(gateId);

        if (newStatus == GateStatus.ASSIGNED && gate.getStatus() == GateStatus.OCCUPIED) {
            throw new GateAlreadyOccupiedException(gate.getGateNumber());
        }

        GateStatus previousStatus = gate.getStatus();
        gate.setStatus(newStatus);
        gate.setUpdatedAt(LocalDateTime.now());

        Gate updatedGate = gateRepository.save(gate);
        logger.info("Gate status updated: {} -> {}", gate.getGateNumber(), newStatus);

        if (newStatus == GateStatus.FREE && previousStatus != GateStatus.FREE) {
            String reason = previousStatus == GateStatus.MAINTENANCE
                    ? "MAINTENANCE_COMPLETED"
                    : previousStatus == GateStatus.OCCUPIED
                    ? "AIRCRAFT_DEPARTED"
                    : "STATUS_CHANGED_TO_FREE";

            eventPublisher.publishEvent(new GateFreedEvent(this, updatedGate, reason));
            logger.info("GateFreedEvent published for gate: {} (changed from {} to FREE)",
                    updatedGate.getGateNumber(), previousStatus);
        }

        return updatedGate;
    }

    @Transactional
    public Gate occupyGate(Long gateId) {
        logger.info("Occupying gate with ID: {}", gateId);
        return updateGateStatus(gateId, GateStatus.OCCUPIED);
    }

    @Transactional
    public Gate freeGate(Long gateId) {
        logger.info("Freeing gate with ID: {}", gateId);
        Gate gate = updateGateStatus(gateId, GateStatus.FREE);

        eventPublisher.publishEvent(new GateFreedEvent(this, gate, "AIRCRAFT_DEPARTED"));
        logger.info("GateFreedEvent published for gate: {}", gate.getGateNumber());

        return gate;
    }

    @Transactional
    public Gate assignGate(Long gateId) {
        logger.info("Assigning gate with ID: {}", gateId);
        return updateGateStatus(gateId, GateStatus.ASSIGNED);
    }

    @Transactional
    public Gate setGateToMaintenance(Long gateId) {
        logger.info("Setting gate to maintenance: {}", gateId);
        return updateGateStatus(gateId, GateStatus.MAINTENANCE);
    }

    @Transactional
    public void deactivateGate(Long gateId) {
        logger.info("Deactivating gate with ID: {}", gateId);

        Gate gate = getGateById(gateId);

        if (gate.getStatus() == GateStatus.OCCUPIED || gate.getStatus() == GateStatus.ASSIGNED) {
            throw new IllegalStateException("Cannot deactivate gate " + gate.getGateNumber() + " while it is in use");
        }

        gate.setIsActive(false);
        gate.setUpdatedAt(LocalDateTime.now());

        gateRepository.save(gate);
        logger.info("Gate deactivated: {}", gate.getGateNumber());
    }

    @Transactional
    public void activateGate(Long gateId) {
        logger.info("Activating gate with ID: {}", gateId);

        Gate gate = getGateById(gateId);
        gate.setIsActive(true);
        gate.setUpdatedAt(LocalDateTime.now());

        gateRepository.save(gate);
        logger.info("Gate activated: {}", gate.getGateNumber());
    }

    @Transactional
    public void deleteGate(Long gateId) {
        logger.info("Deleting gate with ID: {}", gateId);

        Gate gate = getGateById(gateId);

        if (gate.getStatus() == GateStatus.OCCUPIED || gate.getStatus() == GateStatus.ASSIGNED) {
            throw new IllegalStateException("Cannot delete gate " + gate.getGateNumber() + " while it is in use");
        }

        gateRepository.delete(gate);
        logger.info("Gate deleted: {}", gate.getGateNumber());
    }

    @Transactional(readOnly = true)
    public long countGatesByStatus(GateStatus status) {
        return gateRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countAvailableGatesByType(GateType gateType) {
        return gateRepository.countAvailableByType(gateType);
    }

    @Transactional(readOnly = true)
    public boolean isGateAvailable(Long gateId) {
        Gate gate = getGateById(gateId);
        return gate.isAvailable();
    }

    @Transactional(readOnly = true)
    public Gate getGateWithAssignments(Long gateId) {
        return gateRepository.findByIdWithAssignments(gateId)
                .orElseThrow(() -> new GateNotFoundException(gateId));
    }
}
