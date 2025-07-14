package com.bauto.quarkus.writer.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jboss.logging.Logger;


@ApplicationScoped
public class MqttSubscriberService {

    private static final Logger log = Logger.getLogger(MqttSubscriberService.class);

    @ConfigProperty(name = "mqtt.broker")
    String brokerUrl;

    @ConfigProperty(name = "mqtt.topic")
    String topic;

    private MqttClient mqttClient;

    @Inject
    private AsyncFileWriterService fileWriterService;

    @PostConstruct
    void connectAndSubscribe() {
        try {
            mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId(),
                    new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);
            log.infof("Connected to MQTT broker at: %s", brokerUrl);

            mqttClient.subscribe(topic, (receivedTopic, message) -> {
                String payload = new String(message.getPayload());
                log.infof("Received MQTT message: %s", payload);
                fileWriterService.writeLine(payload);
            });

            log.infof("Subscribed to MQTT topic: %s", topic);
        } catch (MqttException e) {
            log.error("Failed to connect or subscribe to MQTT broker", e);
        }
    }

    public void dummyCall() {
        System.out.println("app started");
    }

    @PreDestroy
    void shutdown() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
                log.info("Disconnected from MQTT broker");
            }
        } catch (MqttException e) {
            log.warn("Error while disconnecting from MQTT broker", e);
        }
    }
}
