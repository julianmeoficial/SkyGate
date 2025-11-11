package com.skygate.backend.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(IMqttClient.class)
public class MqttHealthIndicator implements HealthIndicator {

    private final IMqttClient mqttClient;

    public MqttHealthIndicator(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public Health health() {
        if (mqttClient == null) {
            return Health.unknown()
                    .withDetail("mqtt", "Not configured")
                    .build();
        }

        if (mqttClient.isConnected()) {
            return Health.up()
                    .withDetail("mqtt", "Connected")
                    .withDetail("clientId", mqttClient.getClientId())
                    .withDetail("serverURI", mqttClient.getServerURI())
                    .build();
        } else {
            return Health.down()
                    .withDetail("mqtt", "Disconnected")
                    .withDetail("clientId", mqttClient.getClientId())
                    .withDetail("serverURI", mqttClient.getServerURI())
                    .build();
        }
    }
}
