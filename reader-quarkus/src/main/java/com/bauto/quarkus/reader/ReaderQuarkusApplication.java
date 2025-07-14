package com.bauto.quarkus.reader;

import java.nio.file.Files;
import java.nio.file.Path;
import org.jboss.logging.Logger;
import com.bauto.quarkus.reader.service.FileProcessingService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class ReaderQuarkusApplication implements QuarkusApplication {

    public static String[] CLI_ARGS;

    private static final Logger log = Logger.getLogger(ReaderQuarkusApplication.class);

    @Inject
    FileProcessingService service;

    @Override
    public int run(String... args) throws Exception {
        Path file;

        if (args.length > 0) {
            file = Path.of(args[0]);
            log.infof("Processing provided file: %s", file);
        } else {
            file = Path.of("samplefile.txt");
            log.info("No file passed — defaulting to samplefile.txt");
        }

        if (Files.exists(file)) {
            service.processFile(file);
        } else {
            log.errorf("File does not exist: %s", file);
        }

        // Keep app running (like Spring Boot)
        Thread.currentThread().join();
        return 0;
    }


    public static void main(String... args) {
        CLI_ARGS = args;
        Quarkus.run(ReaderQuarkusApplication.class, args);
    }



}
