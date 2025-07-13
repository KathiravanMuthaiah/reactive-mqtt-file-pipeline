package com.bauto.spring.reader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.bauto.spring.reader.service.FileProcessingService;

@SpringBootApplication
public class ReaderSpringbootApplication implements CommandLineRunner {
  @Autowired
  private FileProcessingService service;

  public void run(String... args) {
    if (args.length > 0) {
      Path file = Paths.get(args[0]);
      if (file != null && Files.exists(file)) {
        service.processFile(file);
      }
    } else {
      service.processFile(Paths.get("samplefile.txt"));
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ReaderSpringbootApplication.class, args);
  }

}
