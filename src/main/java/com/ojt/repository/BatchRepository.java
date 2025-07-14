package com.ojt.repository;

import com.ojt.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ojt.entity.Batch;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    long count();
}
