package com.skygate.backend.service.gate;

import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import com.skygate.backend.repository.GateRepository;
import com.skygate.backend.exception.NoAvailableGateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GateAvailabilityService {

    private static final Logger logger = LoggerFactory.getLogger(GateAvailabilityService.class);

    private final GateRepository gateRepository;

    public GateAvailabilityService(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Gate> findAvailableGate(AircraftType aircraftType) {
        logger.info("Searching for available gate for aircraft type: {}", aircraftType);

        GateType primaryGateType = GateType.fromAircraftType(aircraftType);
        List<Gate> availableGates = gateRepository.findAvailableGatesByType(primaryGateType);

        if (!availableGates.isEmpty()) {
            Gate selectedGate = availableGates.get(0);
            logger.info("Found available gate: {} for aircraft type: {}", selectedGate.getGateNumber(), aircraftType);
            return Optional.of(selectedGate);
        }

        if (aircraftType == AircraftType.NARROW_BODY || aircraftType == AircraftType.WIDE_BODY) {
            logger.info("No exact match found, searching for larger compatible gates");
            return findLargerCompatibleGate(aircraftType);
        }

        logger.warn("No available gate found for aircraft type: {}", aircraftType);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Gate> findAvailableGateWithFallback(AircraftType aircraftType) {
        Optional<Gate> gate = findAvailableGate(aircraftType);
        if (gate.isPresent()) {
            return gate;
        }
        throw new NoAvailableGateException(aircraftType);
    }

    private Optional<Gate> findLargerCompatibleGate(AircraftType aircraftType) {
        List<Gate> allAvailableGates = gateRepository.findAllAvailableGatesOrderByTypeDesc();

        for (Gate gate : allAvailableGates) {
            if (aircraftType.isCompatibleWith(gate.getGateType())) {
                logger.info("Found compatible larger gate: {} for aircraft type: {}",
                        gate.getGateNumber(), aircraftType);
                return Optional.of(gate);
            }
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<Gate> findAllAvailableGates() {
        logger.debug("Fetching all available gates");
        return gateRepository.findByStatusAndIsActiveTrue(GateStatus.FREE);
    }

    @Transactional(readOnly = true)
    public List<Gate> findAllAvailableGatesByType(GateType gateType) {
        logger.debug("Fetching all available gates of type: {}", gateType);
        return gateRepository.findAvailableGatesByType(gateType);
    }

    @Transactional(readOnly = true)
    public boolean hasAvailableGate(AircraftType aircraftType) {
        GateType gateType = GateType.fromAircraftType(aircraftType);
        boolean available = gateRepository.existsAvailableGateByType(gateType);

        if (!available && (aircraftType == AircraftType.NARROW_BODY || aircraftType == AircraftType.WIDE_BODY)) {
            available = findLargerCompatibleGate(aircraftType).isPresent();
        }

        logger.debug("Gate availability for {}: {}", aircraftType, available);
        return available;
    }

    @Transactional(readOnly = true)
    public int getAvailableGatesCount() {
        return (int) gateRepository.countByStatus(GateStatus.FREE);
    }

    @Transactional(readOnly = true)
    public int getAvailableGatesCountByType(GateType gateType) {
        return (int) gateRepository.countAvailableByType(gateType);
    }

    @Transactional(readOnly = true)
    public Gate findBestAvailableGate(AircraftType aircraftType, String preferredTerminal) {
        logger.info("Finding best available gate for {} in terminal {}", aircraftType, preferredTerminal);

        List<Gate> availableGates = gateRepository.findAllAvailableGatesOrderByTypeDesc();

        Optional<Gate> terminalGate = availableGates.stream()
                .filter(gate -> aircraftType.isCompatibleWith(gate.getGateType()))
                .filter(gate -> gate.getTerminal() != null && gate.getTerminal().equals(preferredTerminal))
                .findFirst();

        if (terminalGate.isPresent()) {
            logger.info("Found gate in preferred terminal: {}", terminalGate.get().getGateNumber());
            return terminalGate.get();
        }

        Optional<Gate> anyGate = availableGates.stream()
                .filter(gate -> aircraftType.isCompatibleWith(gate.getGateType()))
                .min(Comparator.comparing(gate -> gate.getGateType().ordinal()));

        return anyGate.orElseThrow(() -> new NoAvailableGateException(aircraftType));
    }

    @Transactional(readOnly = true)
    public List<Gate> findAvailableGatesByTerminal(String terminal) {
        logger.debug("Fetching available gates in terminal: {}", terminal);
        return gateRepository.findByTerminalAndStatus(terminal, GateStatus.FREE);
    }
}
