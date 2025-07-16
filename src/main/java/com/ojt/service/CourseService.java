package com.ojt.service;

import com.ojt.dto.CourseDTO;
import com.ojt.entity.Courses;

import java.util.List;

public interface CourseService {
    //Tay Za Lin Htut
    List<Courses> findAll();
    List<Courses> getCoursesByBatchId(Long batchId);
    List<Courses> getCoursesWithoutBatch(Long batchId);
    void assignCoursesToBatch(List<Long> courseIds, Long batchId);
    void unassignCoursesFromBatch(List<Long> courseIds, Long batchId);

    //Nyi Min Htet Lwin
    List<Courses> getAllCourses();
    Courses getCourseById(Long id);
    Courses createCourse(CourseDTO courseDTO);
    Courses updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
    boolean existsByName(String name);

    // NEW method for assigning instructors
    void assignInstructorsToCourse(Long courseId, List<Long> instructorIds);
}
