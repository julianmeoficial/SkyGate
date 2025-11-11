package com.skygate.backend.service.hardware;

import com.skygate.backend.mqtt.MqttPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HardwareService {

    private static final Logger logger = LoggerFactory.getLogger(HardwareService.class);

    private final Optional<MqttPublisher> mqttPublisher;

    public HardwareService(Optional<MqttPublisher> mqttPublisher) {
        this.mqttPublisher = mqttPublisher;
        if (mqttPublisher.isPresent()) {
            logger.info("HardwareService initialized with MQTT support");
        } else {
            logger.warn("HardwareService initialized WITHOUT MQTT support (running in local mode)");
        }
    }

    public void activateLeds(Long gateId, String gateNumber, String color) {
        logger.info("Activating LEDs for gate {}: Color {}", gateNumber, color);

        if (mqttPublisher.isPresent()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "ACTIVATE");
            payload.put("gateId", gateId);
            payload.put("gateNumber", gateNumber);
            payload.put("color", color);
            payload.put("timestamp", System.currentTimeMillis());

            mqttPublisher.get().publishLedControl(payload);
            logger.info("LED activation command sent via MQTT for gate {}", gateNumber);
        } else {
            logger.info("MQTT not available - LED activation simulated for gate {}", gateNumber);
        }
    }

    public void deactivateLeds(Long gateId, String gateNumber) {
        logger.info("Deactivating LEDs for gate {}", gateNumber);

        if (mqttPublisher.isPresent()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "DEACTIVATE");
            payload.put("gateId", gateId);
            payload.put("gateNumber", gateNumber);
            payload.put("timestamp", System.currentTimeMillis());

            mqttPublisher.get().publishLedControl(payload);
            logger.info("LED deactivation command sent via MQTT for gate {}", gateNumber);
        } else {
            logger.info("MQTT not available - LED deactivation simulated for gate {}", gateNumber);
        }
    }

    public void sendGateAssignmentNotification(Long gateId, String gateNumber, Long flightId, String flightNumber) {
        logger.info("Sending gate assignment notification: Flight {} -> Gate {}", flightNumber, gateNumber);

        if (mqttPublisher.isPresent()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "GATE_ASSIGNED");
            payload.put("gateId", gateId);
            payload.put("gateNumber", gateNumber);
            payload.put("flightId", flightId);
            payload.put("flightNumber", flightNumber);
            payload.put("timestamp", System.currentTimeMillis());

            mqttPublisher.get().publishGateAssignment(payload);
            logger.info("Gate assignment notification sent via MQTT");
        } else {
            logger.info("MQTT not available - Gate assignment notification simulated");
        }
    }

    public void updateDisplayScreen(String gateNumber, String flightNumber, String status) {
        logger.info("Updating display screen for gate {}: Flight {} - Status {}", gateNumber, flightNumber, status);

        if (mqttPublisher.isPresent()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "UPDATE_DISPLAY");
            payload.put("gateNumber", gateNumber);
            payload.put("flightNumber", flightNumber);
            payload.put("status", status);
            payload.put("timestamp", System.currentTimeMillis());

            mqttPublisher.get().publishMessage("skygate/display/update", payload);
            logger.info("Display update command sent via MQTT");
        } else {
            logger.info("MQTT not available - Display update simulated");
        }
    }

    public boolean isMqttAvailable() {
        return mqttPublisher.isPresent();
    }
}
