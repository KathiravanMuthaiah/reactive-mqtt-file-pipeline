package com.bauto.spring.reader.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import com.bauto.spring.reader.util.LineTransformer;
import com.bauto.spring.util.CloseableUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class StandaloneFileProcessing {


  public static void main(String[] args) throws IOException {
    processFile(Paths.get("samplefile.txt"));
  }

  public static void processFile(Path filePath) throws IOException {
    System.out.println("Starting processing of file: {}" + filePath);
    System.out.println("Absolute path: " + filePath.toAbsolutePath());
    System.out.println("File exists: " + Files.exists(filePath));
    System.out.println("File size: " + Files.size(filePath) + " bytes");
    AtomicInteger lineCounter = new AtomicInteger();

    try {
      Flux.using(() -> new BufferedReader(new FileReader(filePath.toFile())),
          reader -> Flux.fromStream(reader.lines()), CloseableUtils.safeCloser())
          // .subscribeOn(Schedulers.boundedElastic()).map(String::trim)
          // .filter(line -> !line.isEmpty())
          .doOnNext(rawLine -> {
            // Transform line
            // String transformed = LineTransformer.appendSum(rawLine);
            System.out.println("transformed line:" + rawLine);
            lineCounter.incrementAndGet();
          }).doOnComplete(() -> {
            System.out.println("Completed processing file: " + filePath.getFileName() + "| "
                + lineCounter.get() + " lines");

          }).doOnError(e -> e.printStackTrace()).subscribe();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
