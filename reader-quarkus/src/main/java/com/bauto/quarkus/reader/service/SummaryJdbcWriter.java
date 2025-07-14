package com.bauto.quarkus.reader.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import javax.sql.DataSource;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;

@ApplicationScoped
public class SummaryJdbcWriter {

    private static final Logger log = Logger.getLogger(SummaryJdbcWriter.class);

    private final DataSource dataSource;

    @Inject
    public SummaryJdbcWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void writeSummary(String fileName, int lineCount) {
        String sql = "INSERT INTO fileproc.file_summary (file_name, total_lines) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fileName);
            ps.setInt(2, lineCount);
            ps.executeUpdate();

            log.infof("Inserted summary row for file: %s, lines: %d", fileName, lineCount);

        } catch (Exception e) {
            log.errorf("Failed to insert summary row for file: %s", fileName, e);
        }
    }
}
