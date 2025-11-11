package com.skygate.backend.config;

import com.skygate.backend.util.Constants;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    @Value("${mqtt.broker.url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.client.id:skygate-backend}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    @Value("${mqtt.auto-reconnect:true}")
    private boolean autoReconnect;

    @Value("${mqtt.clean-session:true}")
    private boolean cleanSession;

    @Value("${mqtt.enabled:false}")
    private boolean mqttEnabled;

    @Bean
    @ConditionalOnProperty(name = "mqtt.enabled", havingValue = "true", matchIfMissing = false)
    public IMqttClient mqttClient() {
        try {
            String uniqueClientId = clientId + "-" + System.currentTimeMillis();
            IMqttClient client = new MqttClient(brokerUrl, uniqueClientId);

            MqttConnectOptions options = mqttConnectOptions();
            client.connect(options);

            logger.info("MQTT client connected successfully to broker: {}", brokerUrl);

            return client;

        } catch (MqttException e) {
            logger.warn("Could not connect to MQTT broker at {}: {}. MQTT functionality will be disabled.",
                    brokerUrl, e.getMessage());
            return null;
        }
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(autoReconnect);
        options.setCleanSession(cleanSession);
        options.setConnectionTimeout(Constants.MqttConstants.CONNECTION_TIMEOUT);
        options.setKeepAliveInterval(Constants.MqttConstants.KEEP_ALIVE_INTERVAL);

        if (username != null && !username.isEmpty()) {
            options.setUserName(username);
        }

        if (password != null && !password.isEmpty()) {
            options.setPassword(password.toCharArray());
        }

        logger.info("MQTT connection options configured: broker={}, autoReconnect={}, cleanSession={}",
                brokerUrl, autoReconnect, cleanSession);

        return options;
    }
}
