package com.skygate.backend.config;

import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.GateStatus;
import com.skygate.backend.model.enums.GateType;
import com.skygate.backend.repository.AircraftRepository;
import com.skygate.backend.repository.GateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final GateRepository gateRepository;
    private final AircraftRepository aircraftRepository;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            if (aircraftRepository.count() == 0) {
                log.info("Inicializando Aircraft de prueba...");
                createInitialAircraft();
                log.info("Aircraft creados exitosamente");
            }

            if (gateRepository.count() == 0) {
                log.info("Inicializando Gates de prueba...");
                createInitialGates();
                log.info("Gates creados exitosamente");
            }
        };
    }

    private void createInitialAircraft() {
        List<Aircraft> aircrafts = new ArrayList<>();

        Aircraft narrowBody = new Aircraft();
        narrowBody.setModel("Boeing 737");
        narrowBody.setAircraftType(AircraftType.NARROW_BODY);
        narrowBody.setManufacturer("Boeing");
        narrowBody.setMaxPassengers(180);  // USA maxPassengers en lugar de capacity
        aircrafts.add(narrowBody);

        Aircraft wideBody = new Aircraft();
        wideBody.setModel("Boeing 787");
        wideBody.setAircraftType(AircraftType.WIDE_BODY);
        wideBody.setManufacturer("Boeing");
        wideBody.setMaxPassengers(300);
        aircrafts.add(wideBody);

        Aircraft jumbo = new Aircraft();
        jumbo.setModel("Boeing 747");
        jumbo.setAircraftType(AircraftType.JUMBO);
        jumbo.setManufacturer("Boeing");
        jumbo.setMaxPassengers(450);
        aircrafts.add(jumbo);

        aircraftRepository.saveAll(aircrafts);
        log.info("Creados {} aircraft iniciales", aircrafts.size());
    }

    private void createInitialGates() {
        List<Gate> gates = new ArrayList<>();

        gates.add(createGate("A1", GateType.JUMBO, "A", "Terminal A - Gate 1"));
        gates.add(createGate("A2", GateType.JUMBO, "A", "Terminal A - Gate 2"));
        gates.add(createGate("A3", GateType.JUMBO, "A", "Terminal A - Gate 3"));

        gates.add(createGate("B1", GateType.WIDE_BODY, "B", "Terminal B - Gate 1"));
        gates.add(createGate("B2", GateType.WIDE_BODY, "B", "Terminal B - Gate 2"));
        gates.add(createGate("B3", GateType.WIDE_BODY, "B", "Terminal B - Gate 3"));
        gates.add(createGate("B4", GateType.WIDE_BODY, "B", "Terminal B - Gate 4"));

        gates.add(createGate("C1", GateType.NARROW_BODY, "C", "Terminal C - Gate 1"));
        gates.add(createGate("C2", GateType.NARROW_BODY, "C", "Terminal C - Gate 2"));
        gates.add(createGate("C3", GateType.NARROW_BODY, "C", "Terminal C - Gate 3"));
        gates.add(createGate("C4", GateType.NARROW_BODY, "C", "Terminal C - Gate 4"));
        gates.add(createGate("C5", GateType.NARROW_BODY, "C", "Terminal C - Gate 5"));
        gates.add(createGate("C6", GateType.NARROW_BODY, "C", "Terminal C - Gate 6"));
        gates.add(createGate("C7", GateType.NARROW_BODY, "C", "Terminal C - Gate 7"));
        gates.add(createGate("C8", GateType.NARROW_BODY, "C", "Terminal C - Gate 8"));

        gateRepository.saveAll(gates);
        log.info("Creados {} gates iniciales", gates.size());
    }

    private Gate createGate(String gateNumber, GateType gateType, String terminal, String location) {
        Gate gate = new Gate();
        gate.setGateNumber(gateNumber);
        gate.setGateType(gateType);
        gate.setTerminal(terminal);
        gate.setLocation(location);
        gate.setStatus(GateStatus.FREE);
        gate.setIsActive(true);
        gate.setLedPath("/leds/" + terminal.toLowerCase() + "/" + gateNumber.toLowerCase());
        return gate;
    }
}
