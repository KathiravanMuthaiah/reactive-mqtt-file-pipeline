package com.bauto.spring.writer.service;

import com.bauto.spring.writer.util.FileWriterHelper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class AsyncFileWriterService {

    private static final Logger log = LoggerFactory.getLogger(AsyncFileWriterService.class);

    @Value("${output.path}")
    private String outputFilePath;

    private final Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

    @PostConstruct
    public void initWriter() {
        Flux<String> fileFlux = sink.asFlux();

        fileFlux.subscribe(line -> {
            try {
                FileWriterHelper.appendLine(outputFilePath, line);
                log.info("Written to file: {}", line);
            } catch (Exception e) {
                log.error("Failed to write line to file", e);
            }
        });

        log.info("Async file writer initialized for: {}", outputFilePath);
    }

    public void writeLine(String line) {
        sink.tryEmitNext(line);
    }

    @PreDestroy
    public void shutdown() {
        sink.tryEmitComplete();
        log.info("Shutting down async file writer");
    }
}
