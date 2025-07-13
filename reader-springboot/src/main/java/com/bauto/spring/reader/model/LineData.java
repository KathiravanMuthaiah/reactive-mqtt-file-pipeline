package com.bauto.spring.reader.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "line_data", schema = "fileproc")
public class LineData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "original_line", nullable = false, columnDefinition = "TEXT")
  private String originalLine;

  @Column(name = "transformed_line", nullable = false, columnDefinition = "TEXT")
  private String transformedLine;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public LineData() {
    // default constructor for JPA
  }

  public LineData(String originalLine, String transformedLine) {
    this.originalLine = originalLine;
    this.transformedLine = transformedLine;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getOriginalLine() {
    return originalLine;
  }

  public void setOriginalLine(String originalLine) {
    this.originalLine = originalLine;
  }

  public String getTransformedLine() {
    return transformedLine;
  }

  public void setTransformedLine(String transformedLine) {
    this.transformedLine = transformedLine;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
