package com.bauto.quarkus.reader.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "line_data", schema = "fileproc")
public class LineData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "original_line", nullable = false, columnDefinition = "TEXT")
    public String originalLine;

    @Column(name = "transformed_line", nullable = false, columnDefinition = "TEXT")
    public String transformedLine;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();

    public LineData() {
        // Required by JPA
    }

    public LineData(String originalLine, String transformedLine) {
        this.originalLine = originalLine;
        this.transformedLine = transformedLine;
    }
}
