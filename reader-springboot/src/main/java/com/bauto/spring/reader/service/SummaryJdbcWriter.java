package com.bauto.spring.reader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SummaryJdbcWriter {

  private static final Logger log = LoggerFactory.getLogger(SummaryJdbcWriter.class);

  private final JdbcTemplate jdbcTemplate;

  public SummaryJdbcWriter(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void writeSummary(String fileName, int lineCount) {
    String sql = "INSERT INTO file_summary (file_name, lines_processed) VALUES (?, ?)";
    try {
      jdbcTemplate.update(sql, fileName, lineCount);
      log.info("Inserted summary row for file: {}, lines: {}", fileName, lineCount);
    } catch (Exception e) {
      log.error("Failed to insert summary row for file: {}", fileName, e);
    }
  }
}
