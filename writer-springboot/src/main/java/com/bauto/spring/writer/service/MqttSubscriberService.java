package com.bauto.spring.writer.service;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttSubscriberService {

    private static final Logger log = LoggerFactory.getLogger(MqttSubscriberService.class);

    private final MqttClient mqttClient;
    private final AsyncFileWriterService fileWriterService;

    @Value("${mqtt.topic}")
    private String topic;

    public MqttSubscriberService(MqttClient mqttClient, AsyncFileWriterService fileWriterService) {
        this.mqttClient = mqttClient;
        this.fileWriterService = fileWriterService;
    }

    @PostConstruct
    public void subscribe() throws MqttException {
        log.info("Subscribing to MQTT topic: {}", topic);
        mqttClient.subscribe(topic, (receivedTopic, message) -> {
            String payload = new String(message.getPayload());
            log.info("Received MQTT message: {}", payload);
            fileWriterService.writeLine(payload);
        });

        log.info("Subscribed to MQTT topic: {}", topic);
    }
}
