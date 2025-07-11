package com.ojt.service;

import com.ojt.dto.BatchDTO;
import com.ojt.entity.Batch;
import com.ojt.entity.Course;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchServiceImplementation implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Batch saveBatch(BatchDTO dto) {
        Batch batch = new Batch();
        batch.setName(dto.getName());
        batch.setStartDate(dto.getStartDate());
        batch.setEndDate(dto.getEndDate());

        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            batch.setCourses(courses);
        }

        return batchRepository.save(batch);
    }


    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
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
    public long countTotalBatches(){
        return batchRepository.count();
    }

}
