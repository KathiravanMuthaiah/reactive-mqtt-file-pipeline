package com.bauto.spring.reader.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisherService {

  private static final Logger log = LoggerFactory.getLogger(MqttPublisherService.class);

  @Value("${mqtt.broker}")
  private String brokerUrl;

  @Value("${mqtt.topic}")
  private String topic;

  private IMqttClient mqttClient;

  @PostConstruct
  public void init() {
    try {
      String clientId = MqttClient.generateClientId();
      mqttClient = new MqttClient(brokerUrl, clientId);
      MqttConnectOptions options = new MqttConnectOptions();
      options.setAutomaticReconnect(true);
      options.setCleanSession(true);
      mqttClient.connect(options);
      log.info("Connected to MQTT broker at {}", brokerUrl);
    } catch (MqttException e) {
      log.error("MQTT connection failed", e);
    }
  }

  public void publish(String message) {
    if (mqttClient == null || !mqttClient.isConnected()) {
      log.warn("MQTT client not connected. Skipping message: {}", message);
      return;
    }

    try {
      MqttMessage mqttMessage = new MqttMessage(message.getBytes());
      mqttMessage.setQos(1);
      mqttClient.publish(topic, mqttMessage);
      log.info("Published message to MQTT topic '{}': {}", topic, message);
    } catch (MqttException e) {
      log.error("Failed to publish message to MQTT", e);
    }
  }

  @PreDestroy
  public void shutdown() {
    try {
      if (mqttClient != null && mqttClient.isConnected()) {
        mqttClient.disconnect();
        mqttClient.close();
        log.info("Disconnected from MQTT broker");
      }
    } catch (MqttException e) {
      log.error("Error disconnecting MQTT client", e);
    }
  }
}
