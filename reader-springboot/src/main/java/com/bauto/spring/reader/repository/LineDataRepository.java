package com.bauto.spring.reader.repository;

import com.bauto.spring.reader.model.LineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineDataRepository extends JpaRepository<LineData, Long> {
  // No custom methods yet — basic CRUD is sufficient for now
}
