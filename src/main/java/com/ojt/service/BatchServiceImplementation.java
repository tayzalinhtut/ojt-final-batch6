package com.ojt.service;

import com.ojt.dto.BatchDTO;
import com.ojt.entity.Batch;
import com.ojt.entity.Courses;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CourseRepository;
import com.ojt.repository.OJTRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BatchServiceImplementation implements BatchService {
    @Autowired
    SystemUserService systemUserService;
    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OJTRepository ojtRepository;

    @Override
    @Transactional
    public Batch saveBatch(BatchDTO dto) {
        Batch batch = new Batch();
        batch.setName(dto.getName());
        batch.setStartDate(dto.getStartDate());
        batch.setEndDate(dto.getEndDate());

        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Courses> courses = courseRepository.findAllById(dto.getCourseIds());
            batch.setCourses(courses);
        }

        return batchRepository.save(batch);
    }

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAllWithCourses(); // updated
    }

    @Override
    public Optional<Batch> getBatchById(Long id) {
        return batchRepository.findById(id);
    }

    @Override
    public Batch updateBatch(Long id, BatchDTO batchDTO) {
        return batchRepository.findById(id).map(batch -> {
            batch.setName(batchDTO.getName());
            batch.setStartDate(batchDTO.getStartDate());
            batch.setEndDate(batchDTO.getEndDate());
            return batchRepository.save(batch);
        }).orElseThrow(() -> new RuntimeException("Batch not found with id " + id));
    }

    @Override
    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }

    @Override
    public long countTotalBatches() {
        return batchRepository.count();
    }
}
