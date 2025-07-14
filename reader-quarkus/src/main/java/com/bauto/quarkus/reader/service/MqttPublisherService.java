package com.bauto.quarkus.reader.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jboss.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MqttPublisherService {

    private static final Logger log = Logger.getLogger(MqttPublisherService.class);

    @ConfigProperty(name = "mqtt.topic")
    private String topic;

    @ConfigProperty(name = "mqtt.broker")
    private String brokerUrl;

    private MqttClient client;

    // public MqttPublisherService() {
    // this.broker = System.getenv().getOrDefault("MQTT_BROKER_HOST", "localhost");
    // this.topic = System.getenv().getOrDefault("MQTT_TOPIC", "file.processed.line");
    // }

    @PostConstruct
    public void init() {
        try {
            String clientId = MqttClient.generateClientId();
            client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);
            log.infof("Connected to MQTT broker at: %s", brokerUrl);

        } catch (MqttException e) {
            log.error("Failed to connect to MQTT broker", e);
        }
    }

    public void publish(String message) {
        if (client == null || !client.isConnected()) {
            log.warn("MQTT client not connected — skipping publish");
            return;
        }

        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1);
            client.publish(topic, mqttMessage);
            log.infof("Published to MQTT topic [%s]: %s", topic, message);
        } catch (MqttException e) {
            log.error("Failed to publish message to MQTT", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
                log.info("MQTT client disconnected");
            }
        } catch (MqttException e) {
            log.warn("Error during MQTT client shutdown", e);
        }
    }
}
