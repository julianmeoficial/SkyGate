package com.skygate.backend.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skygate.backend.util.Constants;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@ConditionalOnBean(IMqttClient.class)
public class MqttSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriber.class);

    private final IMqttClient mqttClient;
    private final ObjectMapper objectMapper;
    private final MqttMessageHandler messageHandler;

    public MqttSubscriber(IMqttClient mqttClient, ObjectMapper objectMapper, MqttMessageHandler messageHandler) {
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
        this.messageHandler = messageHandler;
    }

    @PostConstruct
    public void init() {
        subscribeToTopics();
    }

    private void subscribeToTopics() {
        try {
            if (!mqttClient.isConnected()) {
                logger.info("Connecting MQTT client for subscriptions...");
                mqttClient.connect();
            }

            subscribeToTopic(Constants.MqttConstants.TOPIC_SENSOR_DETECTION, this::handleSensorDetection);
            subscribeToTopic(Constants.MqttConstants.TOPIC_GATE_STATUS, this::handleGateStatus);

            logger.info("Successfully subscribed to all MQTT topics");

        } catch (MqttException e) {
            logger.error("Error subscribing to MQTT topics: {}", e.getMessage(), e);
        }
    }

    private void subscribeToTopic(String topic, IMqttMessageListener listener) throws MqttException {
        mqttClient.subscribe(topic, Constants.MqttConstants.QOS_LEVEL, listener);
        logger.info("Subscribed to MQTT topic: {}", topic);
    }

    private void handleSensorDetection(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            logger.info("Received sensor detection message from topic {}: {}", topic, payload);

            Map<String, Object> data = objectMapper.readValue(payload, Map.class);
            messageHandler.handleSensorDetection(data);

        } catch (Exception e) {
            logger.error("Error processing sensor detection message: {}", e.getMessage(), e);
        }
    }

    private void handleGateStatus(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            logger.info("Received gate status message from topic {}: {}", topic, payload);

            Map<String, Object> data = objectMapper.readValue(payload, Map.class);
            messageHandler.handleGateStatus(data);

        } catch (Exception e) {
            logger.error("Error processing gate status message: {}", e.getMessage(), e);
        }
    }

    public void unsubscribeFromTopic(String topic) {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.unsubscribe(topic);
                logger.info("Unsubscribed from MQTT topic: {}", topic);
            }
        } catch (MqttException e) {
            logger.error("Error unsubscribing from topic {}: {}", topic, e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
                logger.info("MQTT client disconnected on shutdown");
            }
        } catch (MqttException e) {
            logger.error("Error disconnecting MQTT client on shutdown: {}", e.getMessage(), e);
        }
    }
}
