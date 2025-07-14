package com.ojt.service;

import com.ojt.entity.Instructor;
import com.ojt.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorServiceImplementation implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + id));
    }

    // Htet Wai Yan Soe
    @Override
    public Instructor findByName(String name) {
        // TODO Auto-generated method stub
        return instructorRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Instructor not found with name: " + name));

    }

    @Override
    public Instructor findByEmailOrStaffId(String input) {

        Instructor instructor = instructorRepository.findByEmail(input);
        if (instructor == null) {
            instructor = instructorRepository.findByStaffId(Long.parseLong(input));
        }
        return instructor;
    }
}