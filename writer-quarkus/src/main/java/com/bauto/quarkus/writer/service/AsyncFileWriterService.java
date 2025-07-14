package com.bauto.quarkus.writer.service;

import java.util.concurrent.LinkedBlockingQueue;
import org.jboss.logging.Logger;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AsyncFileWriterService {

    private static final Logger log = Logger.getLogger(AsyncFileWriterService.class);

    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    @PostConstruct
    void init() {
        Multi.createFrom().emitter(emitter -> {
            new Thread(() -> {
                while (running) {
                    try {
                        String line = queue.take();
                        System.out.println(line);
                        log.infof("Written to stdout: %s", line);
                    } catch (Exception e) {
                        log.error("Failed to write line to stdout", e);
                    }
                }
                emitter.complete();
            }, "AsyncWriterThread").start();
        }, BackPressureStrategy.BUFFER).subscribe().with(item -> {
        });

    }

    public void writeLine(String line) {
        queue.offer(line);
    }

    @PreDestroy
    void shutdown() {
        running = false;
        log.info("Shutting down async file writer");
    }
}
