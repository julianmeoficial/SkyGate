package com.skygate.backend.mqtt;

import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.entity.Gate;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.service.automata.AutomataService;
import com.skygate.backend.service.flight.FlightDetectionService;
import com.skygate.backend.service.gate.GateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import java.util.Map;

@Component
@ConditionalOnBean(IMqttClient.class)
public class MqttMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandler.class);

    private final FlightDetectionService flightDetectionService;
    private final GateService gateService;
    private final AutomataService automataService;

    public MqttMessageHandler(
            @Lazy FlightDetectionService flightDetectionService,
            GateService gateService,
            @Lazy AutomataService automataService) {
        this.flightDetectionService = flightDetectionService;
        this.gateService = gateService;
        this.automataService = automataService;
    }

    public void handleSensorDetection(Map<String, Object> data) {
        logger.info("Processing sensor detection: {}", data);

        try {
            String action = (String) data.get("action");

            if ("AIRCRAFT_DETECTED".equals(action)) {
                handleAircraftDetection(data);
            } else if ("ARRIVAL_DETECTED".equals(action)) {
                handleArrivalDetection(data);
            } else if ("DEPARTURE_DETECTED".equals(action)) {
                handleDepartureDetection(data);
            } else {
                logger.warn("Unknown sensor action: {}", action);
            }

        } catch (Exception e) {
            logger.error("Error handling sensor detection: {}", e.getMessage(), e);
        }
    }

    private void handleAircraftDetection(Map<String, Object> data) {
        String flightNumber = (String) data.get("flightNumber");
        String aircraftTypeStr = (String) data.get("aircraftType");
        String origin = (String) data.getOrDefault("origin", "Unknown");
        String destination = (String) data.getOrDefault("destination", "Unknown");
        String airline = (String) data.getOrDefault("airline", "Unknown");

        AircraftType aircraftType = AircraftType.fromString(aircraftTypeStr);

        logger.info("Aircraft detected: Flight {} - Type {}", flightNumber, aircraftType);

        Flight detectedFlight = flightDetectionService.detectFlightByAircraftType(
                flightNumber, aircraftType, origin, destination, airline);

        logger.info("Flight detection processed successfully: {}", detectedFlight.getFlightNumber());
    }

    private void handleArrivalDetection(Map<String, Object> data) {
        Object flightIdObj = data.get("flightId");

        if (flightIdObj == null) {
            logger.warn("Flight ID not provided in arrival detection");
            return;
        }

        Long flightId = Long.valueOf(flightIdObj.toString());

        logger.info("Aircraft arrival detected for flight ID: {}", flightId);

        try {
            Flight flight = automataService.getFlightById(flightId);
            automataService.processAircraftArrival(flight);
            logger.info("Arrival processed successfully for flight: {}", flight.getFlightNumber());
        } catch (Exception e) {
            logger.error("Error processing arrival for flight ID {}: {}", flightId, e.getMessage(), e);
        }
    }

    private void handleDepartureDetection(Map<String, Object> data) {
        Object flightIdObj = data.get("flightId");

        if (flightIdObj == null) {
            logger.warn("Flight ID not provided in departure detection");
            return;
        }

        Long flightId = Long.valueOf(flightIdObj.toString());

        logger.info("Aircraft departure detected for flight ID: {}", flightId);

        try {
            Flight flight = automataService.getFlightById(flightId);
            automataService.processAircraftDeparture(flight);
            logger.info("Departure processed successfully for flight: {}", flight.getFlightNumber());
        } catch (Exception e) {
            logger.error("Error processing departure for flight ID {}: {}", flightId, e.getMessage(), e);
        }
    }

    public void handleGateStatus(Map<String, Object> data) {
        logger.info("Processing gate status update: {}", data);

        try {
            String action = (String) data.get("action");

            if ("STATUS_REQUEST".equals(action)) {
                handleStatusRequest(data);
            } else if ("MANUAL_OVERRIDE".equals(action)) {
                handleManualOverride(data);
            } else {
                logger.warn("Unknown gate status action: {}", action);
            }

        } catch (Exception e) {
            logger.error("Error handling gate status: {}", e.getMessage(), e);
        }
    }

    private void handleStatusRequest(Map<String, Object> data) {
        Object gateIdObj = data.get("gateId");

        if (gateIdObj == null) {
            logger.warn("Gate ID not provided in status request");
            return;
        }

        Long gateId = Long.valueOf(gateIdObj.toString());

        logger.info("Gate status requested for gate ID: {}", gateId);

        try {
            Gate gate = gateService.getGateById(gateId);
            logger.info("Gate status: {} - {}", gate.getGateNumber(), gate.getStatus());
        } catch (Exception e) {
            logger.error("Error retrieving gate status for gate ID {}: {}", gateId, e.getMessage(), e);
        }
    }

    private void handleManualOverride(Map<String, Object> data) {
        Object gateIdObj = data.get("gateId");
        String statusStr = (String) data.get("status");

        if (gateIdObj == null || statusStr == null) {
            logger.warn("Gate ID or status not provided in manual override");
            return;
        }

        Long gateId = Long.valueOf(gateIdObj.toString());

        logger.info("Manual override requested for gate ID {}: New status {}", gateId, statusStr);

        try {
            logger.info("Manual override processed for gate ID: {}", gateId);
        } catch (Exception e) {
            logger.error("Error processing manual override for gate ID {}: {}", gateId, e.getMessage(), e);
        }
    }
}
