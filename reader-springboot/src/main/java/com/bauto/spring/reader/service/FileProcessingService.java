package com.bauto.spring.reader.service;

import com.bauto.spring.reader.model.LineData;
import com.bauto.spring.reader.repository.LineDataRepository;
import com.bauto.spring.reader.util.LineTransformer;
import com.bauto.spring.util.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileProcessingService {

  private static final Logger log = LoggerFactory.getLogger(FileProcessingService.class);

  private final LineDataRepository lineDataRepository;
  private final MqttPublisherService mqttPublisherService;
  private final SummaryJdbcWriter summaryJdbcWriter;

  public FileProcessingService(LineDataRepository lineDataRepository,
      MqttPublisherService mqttPublisherService, SummaryJdbcWriter summaryJdbcWriter) {
    this.lineDataRepository = lineDataRepository;
    this.mqttPublisherService = mqttPublisherService;
    this.summaryJdbcWriter = summaryJdbcWriter;
  }

  public void processFile(Path filePath) {
    log.info("Starting processing of file: {}", filePath);

    AtomicInteger lineCounter = new AtomicInteger();

    try {
      Flux.using(() -> new BufferedReader(new FileReader(filePath.toFile())),
          reader -> Flux.fromStream(reader.lines()), CloseableUtils.safeCloser())
          .subscribeOn(Schedulers.boundedElastic()).map(String::trim)
          .filter(line -> !line.isEmpty()).doOnNext(rawLine -> {
            // Save to DB (JPA)
            lineDataRepository.save(new LineData(rawLine));

            // Transform line
            String transformed = LineTransformer.appendSum(rawLine);

            // Publish to MQTT
            mqttPublisherService.publish(transformed);

            lineCounter.incrementAndGet();
          }).doOnComplete(() -> {
            log.info("Completed processing file: {} | {} lines", filePath.getFileName(),
                lineCounter.get());
            summaryJdbcWriter.writeSummary(filePath.getFileName().toString(), lineCounter.get());
          }).doOnError(e -> log.error("Error during file processing", e)).subscribe();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
