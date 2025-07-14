package com.bauto.quarkus.reader.repository;

import com.bauto.quarkus.reader.model.LineData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LineDataRepository implements PanacheRepository<LineData> {
    // Inherits: persist(), findAll(), findById(), delete(), etc.
}
