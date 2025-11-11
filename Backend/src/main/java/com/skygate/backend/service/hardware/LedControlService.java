package com.skygate.backend.service.hardware;

import com.skygate.backend.mqtt.MqttPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LedControlService {

    private static final Logger logger = LoggerFactory.getLogger(LedControlService.class);

    private final Optional<MqttPublisher> mqttPublisher;
    private final Map<Long, LedState> ledStates = new ConcurrentHashMap<>();

    public LedControlService(Optional<MqttPublisher> mqttPublisher) {
        this.mqttPublisher = mqttPublisher;
        if (mqttPublisher.isPresent()) {
            logger.info("LedControlService initialized with MQTT support");
        } else {
            logger.warn("LedControlService initialized WITHOUT MQTT support (running in local mode)");
        }
    }

    public void activateGreenLed(Long gateId, String gateNumber) {
        logger.info("Activating GREEN LED for gate {}", gateNumber);
        activateLed(gateId, gateNumber, "GREEN");
        updateLedState(gateId, true, "GREEN");
    }

    public void activateYellowLed(Long gateId, String gateNumber) {
        logger.info("Activating YELLOW LED for gate {}", gateNumber);
        activateLed(gateId, gateNumber, "YELLOW");
        updateLedState(gateId, true, "YELLOW");
    }

    public void deactivateAllLeds(Long gateId, String gateNumber) {
        logger.info("Deactivating all LEDs for gate {}", gateNumber);
        deactivateLed(gateId, gateNumber);
        updateLedState(gateId, false, null);
    }

    private void activateLed(Long gateId, String gateNumber, String color) {
        if (mqttPublisher.isPresent()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "ACTIVATE");
            payload.put("gateId", gateId);
            payload.put("gateNumber", gateNumber);
            payload.put("color", color);
            payload.put("timestamp", System.currentTimeMillis());

            mqttPublisher.get().publishLedControl(payload);
            logger.info("LED activation command sent via MQTT: {} for gate {}", color, gateNumber);
        } else {
            logger.info("MQTT not available - LED activation simulated: {} for gate {}", color, gateNumber);
        }
    }

    private void deactivateLed(Long gateId, String gateNumber) {
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

    private void updateLedState(Long gateId, boolean active, String color) {
        LedState state = new LedState(active, color, System.currentTimeMillis());
        ledStates.put(gateId, state);
        logger.debug("LED state updated for gate {}: active={}, color={}", gateId, active, color);
    }

    public LedState getLedState(Long gateId) {
        return ledStates.getOrDefault(gateId, new LedState(false, null, 0L));
    }

    public Map<Long, LedState> getAllLedStates() {
        return new HashMap<>(ledStates);
    }

    public boolean isMqttAvailable() {
        return mqttPublisher.isPresent();
    }

    public static class LedState {
        private final boolean active;
        private final String color;
        private final long timestamp;

        public LedState(boolean active, String color, long timestamp) {
            this.active = active;
            this.color = color;
            this.timestamp = timestamp;
        }

        public boolean isActive() {
            return active;
        }

        public String getColor() {
            return color;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
