package com.bauto.spring.reader.controller;

import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bauto.spring.reader.service.FileProcessingService;

@RestController
@RequestMapping("/reader")
public class FileController {

  @Autowired
  private FileProcessingService service;

  @PostMapping("/trigger")
  public ResponseEntity<String> trigger(@RequestParam String filepath) {
    service.processFile(Paths.get(filepath));
    return ResponseEntity.ok("Triggered for: " + filepath);
  }
}
