package com.ojt.service;

import com.ojt.dto.CourseDTO;
import com.ojt.entity.Course;

import java.util.List;

public interface CourseService {
    //Tay Za Lin Htut
    List<Course> findAll();
    List<Course> getCoursesByBatchId(Long batchId);
    List<Course> getCoursesWithoutBatch(Long batchId);
    void assignCoursesToBatch(List<Long> courseIds, Long batchId);
    void unassignCoursesFromBatch(List<Long> courseIds, Long batchId);

    //Nyi Min Htet Lwin
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    Course createCourse(CourseDTO courseDTO);
    Course updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
    boolean existsByName(String name);

    // NEW method for assigning instructors
    void assignInstructorsToCourse(Long courseId, List<Long> instructorIds);
}
