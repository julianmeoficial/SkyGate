package com.skygate.backend.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skygate.backend.util.Constants;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@ConditionalOnBean(IMqttClient.class)
public class MqttPublisher {

    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    private final IMqttClient mqttClient;
    private final ObjectMapper objectMapper;

    public MqttPublisher(IMqttClient mqttClient, ObjectMapper objectMapper) {
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
    }

    public void publishGateAssignment(Map<String, Object> payload) {
        publishMessage(Constants.MqttConstants.TOPIC_GATE_ASSIGNMENT, payload);
    }

    public void publishLedControl(Map<String, Object> payload) {
        publishMessage(Constants.MqttConstants.TOPIC_LED_CONTROL, payload);
    }

    public void publishGateStatus(Map<String, Object> payload) {
        publishMessage(Constants.MqttConstants.TOPIC_GATE_STATUS, payload);
    }

    public void publishSensorDetection(Map<String, Object> payload) {
        publishMessage(Constants.MqttConstants.TOPIC_SENSOR_DETECTION, payload);
    }

    public void publishMessage(String topic, Map<String, Object> payload) {
        try {
            if (!mqttClient.isConnected()) {
                logger.warn("MQTT client is not connected. Attempting to reconnect...");
                mqttClient.connect();
            }

            String jsonPayload = objectMapper.writeValueAsString(payload);

            MqttMessage message = new MqttMessage(jsonPayload.getBytes(StandardCharsets.UTF_8));
            message.setQos(Constants.MqttConstants.QOS_LEVEL);
            message.setRetained(false);

            mqttClient.publish(topic, message);

            logger.info("Published message to topic {}: {}", topic, jsonPayload);

        } catch (MqttException e) {
            logger.error("Error publishing MQTT message to topic {}: {}", topic, e.getMessage(), e);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing payload to JSON: {}", e.getMessage(), e);
        }
    }

    public void publishRawMessage(String topic, String payload) {
        try {
            if (!mqttClient.isConnected()) {
                logger.warn("MQTT client is not connected. Attempting to reconnect...");
                mqttClient.connect();
            }

            MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
            message.setQos(Constants.MqttConstants.QOS_LEVEL);
            message.setRetained(false);

            mqttClient.publish(topic, message);

            logger.info("Published raw message to topic {}: {}", topic, payload);

        } catch (MqttException e) {
            logger.error("Error publishing raw MQTT message to topic {}: {}", topic, e.getMessage(), e);
        }
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public void disconnect() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
                logger.info("MQTT client disconnected successfully");
            }
        } catch (MqttException e) {
            logger.error("Error disconnecting MQTT client: {}", e.getMessage(), e);
        }
    }

    public void reconnect() {
        try {
            if (!mqttClient.isConnected()) {
                mqttClient.connect();
                logger.info("MQTT client reconnected successfully");
            }
        } catch (MqttException e) {
            logger.error("Error reconnecting MQTT client: {}", e.getMessage(), e);
        }
    }
}
