package com.bauto.spring.reader.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "line_data")
public class LineData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "raw_line", nullable = false)
  private String rawLine;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  public LineData() {
    // Default constructor for JPA
  }

  public LineData(String rawLine) {
    this.rawLine = rawLine;
    this.createdAt = Instant.now();
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public String getRawLine() {
    return rawLine;
  }

  public void setRawLine(String rawLine) {
    this.rawLine = rawLine;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
