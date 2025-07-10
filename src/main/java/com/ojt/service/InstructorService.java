package com.ojt.service;

import com.ojt.entity.Instructor;
import java.util.List;

public interface InstructorService {
    List<Instructor> getAllInstructors();
    // You might also need getInstructorById if you plan to link to instructor details
    Instructor getInstructorById(Long id);
}