package com.bauto.quarkus.writer;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import com.bauto.quarkus.writer.service.MqttSubscriberService;
import com.bauto.quarkus.writer.service.AsyncFileWriterService;

@QuarkusMain
public class WriterQuarkusApplication implements QuarkusApplication {

  private static final Logger log = Logger.getLogger(WriterQuarkusApplication.class);

  @Inject
  MqttSubscriberService mqttSubscriberService;

  @Inject
  AsyncFileWriterService asyncFileWriterService;

  @Override
  public int run(String... args) throws Exception {
    log.info("🔥 WriterQuarkusApplication.run() triggered");
    log.info("Warming up beans: " + mqttSubscriberService + ", " + asyncFileWriterService);

    // Quarkus stays running until manually stopped
    Quarkus.waitForExit();
    return 0;
  }

  public static void main(String... args) {
    Quarkus.run(WriterQuarkusApplication.class, args);
  }
}
