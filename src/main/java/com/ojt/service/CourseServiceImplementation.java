package com.ojt.service;

import com.ojt.dto.CourseDTO;
import com.ojt.entity.Batch;
import com.ojt.entity.Course;
import com.ojt.entity.Instructor;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CourseRepository;
import com.ojt.repository.InstructorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImplementation implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public List<Course> getCoursesByBatchId(Long batchId) {
        return courseRepository.findByBatches_Id(batchId);
    }

    @Override
    public List<Course> getCoursesWithoutBatch(Long batchId) {
        return courseRepository.findCoursesNotInBatchOrUnassigned(batchId);
    }

    @Override
    public void assignCoursesToBatch(List<Long> courseIds, Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch ID"));

        List<Course> courses = courseRepository.findAllById(courseIds);
        for (Course course : courses) {
            if (!course.getBatches().contains(batch)) {
                course.getBatches().add(batch);
                batch.getCourses().add(course);
            }
        }
        courseRepository.saveAll(courses);
    }

    @Override
    public void unassignCoursesFromBatch(List<Long> courseIds, Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch ID"));

        List<Course> courses = courseRepository.findAllById(courseIds);
        for (Course course : courses) {
            course.getBatches().remove(batch);
            batch.getCourses().remove(course);
        }
        courseRepository.saveAll(courses);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
    }

    @Override
    @Transactional
    public Course createCourse(CourseDTO courseDTO) {
        if (courseRepository.existsByName(courseDTO.getName())) {
            throw new RuntimeException("Course name '" + courseDTO.getName() + "' already exists.");
        }

        Course course = new Course();
        course.setName(courseDTO.getName());

        if (courseDTO.getInstructorIds() != null && !courseDTO.getInstructorIds().isEmpty()) {
            Set<Instructor> instructors = new HashSet<>(instructorRepository.findAllById(courseDTO.getInstructorIds()));
            course.setInstructors(instructors.stream().toList());
            for (Instructor instructor : instructors) {
                instructor.getCourses().add(course);
            }
        }

        if (courseDTO.getBatchIds() != null && !courseDTO.getBatchIds().isEmpty()) {
            Set<Batch> batches = new HashSet<>(batchRepository.findAllById(courseDTO.getBatchIds()));
            course.setBatches(batches);
            for (Batch batch : batches) {
                batch.getCourses().add(course);
            }
        }

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = getCourseById(id);

        if (!course.getName().equalsIgnoreCase(courseDTO.getName()) &&
                courseRepository.existsByName(courseDTO.getName())) {
            throw new RuntimeException("Course name '" + courseDTO.getName() + "' already exists for another course.");
        }

        course.setName(courseDTO.getName());

        // Update instructors
        for (Instructor instructor : new HashSet<>(course.getInstructors())) {
            instructor.getCourses().remove(course);
        }
        course.getInstructors().clear();

        if (courseDTO.getInstructorIds() != null && !courseDTO.getInstructorIds().isEmpty()) {
            Set<Instructor> newInstructors = new HashSet<>(instructorRepository.findAllById(courseDTO.getInstructorIds()));
            course.setInstructors(newInstructors.stream().toList());
            for (Instructor instructor : newInstructors) {
                instructor.getCourses().add(course);
            }
        }

        // Update batches
        for (Batch batch : new HashSet<>(course.getBatches())) {
            batch.getCourses().remove(course);
        }
        course.getBatches().clear();

        if (courseDTO.getBatchIds() != null && !courseDTO.getBatchIds().isEmpty()) {
            Set<Batch> newBatches = new HashSet<>(batchRepository.findAllById(courseDTO.getBatchIds()));
            course.setBatches(newBatches);
            for (Batch batch : newBatches) {
                batch.getCourses().add(course);
            }
        }

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);

        for (Instructor instructor : new HashSet<>(course.getInstructors())) {
            instructor.getCourses().remove(course);
        }
        course.getInstructors().clear();

        for (Batch batch : new HashSet<>(course.getBatches())) {
            batch.getCourses().remove(course);
        }
        course.getBatches().clear();

        courseRepository.delete(course);
    }

    @Override
    public boolean existsByName(String name) {
        return courseRepository.existsByName(name);
    }

    @Override
    @Transactional
    public void assignInstructorsToCourse(Long courseId, List<Long> instructorIds) {
        Course course = getCourseById(courseId);

        for (Instructor instructor : new HashSet<>(course.getInstructors())) {
            instructor.getCourses().remove(course);
        }
        course.getInstructors().clear();

        if (instructorIds != null && !instructorIds.isEmpty()) {
            List<Instructor> newInstructors = instructorRepository.findAllById(instructorIds);
            course.setInstructors(newInstructors);
            for (Instructor instructor : newInstructors) {
                instructor.getCourses().add(course);
            }
        }

        courseRepository.save(course);
    }
}
