package com.bauto.quarkus.reader.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import org.jboss.logging.Logger;
import com.bauto.quarkus.reader.model.LineData;
import com.bauto.quarkus.reader.repository.LineDataRepository;
import com.bauto.quarkus.reader.util.LineTransformer;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileProcessingService {

    private static final Logger log = Logger.getLogger(FileProcessingService.class);

    private final LineDataRepository lineDataRepository;
    private final MqttPublisherService mqttPublisherService;
    private final SummaryJdbcWriter summaryJdbcWriter;

    public FileProcessingService(LineDataRepository lineDataRepository,
            MqttPublisherService mqttPublisherService, SummaryJdbcWriter summaryJdbcWriter) {
        this.lineDataRepository = lineDataRepository;
        this.mqttPublisherService = mqttPublisherService;
        this.summaryJdbcWriter = summaryJdbcWriter;
    }

    @Transactional
    public void processFile(Path filePath) {
        log.infof("Starting processing of file: %s", filePath);

        AtomicInteger lineCounter = new AtomicInteger();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));

            Multi.createFrom().items(reader.lines()).onTermination().invoke(() -> {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.warn("Failed to close reader", e);
                }
            }).map(String::trim).filter(line -> !line.isEmpty()).invoke(rawLine -> {
                String transformed = LineTransformer.appendSum(rawLine);

                // Save to DB
                lineDataRepository.persist(new LineData(rawLine, transformed));

                // MQTT publish
                mqttPublisherService.publish(transformed);

                lineCounter.incrementAndGet();
            }).onCompletion().invoke(() -> {
                log.infof("Completed file %s | %d lines", filePath.getFileName(),
                        lineCounter.get());
                summaryJdbcWriter.writeSummary(filePath.getFileName().toString(),
                        lineCounter.get());
            }).onFailure()
                    .invoke(e -> log.errorf("Error during file processing: %s", e.getMessage()))
                    .subscribe().with(x -> {
                    });
        } catch (Exception e) {
            log.errorf("Failed to process file: %s", filePath, e);
        }
    }
}
