package com.ojt.service;

import com.ojt.dto.BatchDTO;
import com.ojt.entity.Batch;

import java.util.List;
import java.util.Optional;

public interface BatchService {
    List<Batch> getAllBatches();
    Optional<Batch> getBatchById(Long id);
    Batch updateBatch(Long id, BatchDTO batch);
    void deleteBatch(Long id);
    Batch saveBatch(BatchDTO batchDto);

    long countTotalBatches();
}
