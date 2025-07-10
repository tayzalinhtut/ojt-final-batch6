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
import java.util.stream.Collectors;

@Service
public class CourseServiceImplementation implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override //Tay Za Lin Htut
    public List<Course> getCoursesByBatchId(Long batchId) {
        return courseRepository.findByBatches_Id(batchId);
    }


    @Override //Tay Za Lin Htut
    public List<Course> getCoursesWithoutBatch(Long batchId) {
        return courseRepository.findCoursesNotInBatchOrUnassigned(batchId);
    }


    @Override //Tay Za Lin Htut
    public void assignCoursesToBatch(List<Long> courseIds, Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch ID"));

        List<Course> courses = courseRepository.findAllById(courseIds);
        for (Course course : courses) {
            if (!course.getBatches().contains(batch)) {
                course.getBatches().add(batch);
            }
        }
        courseRepository.saveAll(courses);
    }
    @Override //Tay Za Lin Htut
    public void unassignCoursesFromBatch(List<Long> courseIds, Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch ID"));

        List<Course> courses = courseRepository.findAllById(courseIds);
        for (Course course : courses) {
            course.getBatches().remove(batch);
        }
        courseRepository.saveAll(courses);
    }
    @Override //Tay Za Lin Htut
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public List<Course> getAllCourses() {
        // Ensure instructors are fetched if needed immediately on course list page
        // If not using eager fetching or a custom query in repository, you might need a custom method.
        // For simplicity, let's assume default fetching or that the Thymeleaf template will lazily load.
        // If your CourseRepository.findAllWithInstructors() works, keep it.
        return courseRepository.findAll(); // Assuming find all might fetch or lazy load
    }

    @Override
    @Transactional
    public Course getCourseById(Long id) {
        // Use findById for simplicity; if you specifically need eager loading of instructors
        // when fetching a single course, you might need a custom query in your repository.
        // For assigning instructors, the default fetching should be fine as we modify the collection.
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
            Set<Instructor> instructors = new HashSet<>(
                    instructorRepository.findAllById(courseDTO.getInstructorIds())
            );
            // Use helper method to maintain bidirectional relationship
            instructors.forEach(course::addInstructor);
        }

        if (courseDTO.getBatchIds() != null && !courseDTO.getBatchIds().isEmpty()) {
            Set<Batch> batches = new HashSet<>(
                    batchRepository.findAllById(courseDTO.getBatchIds())
            );
            // Use helper method to maintain bidirectional relationship
            batches.forEach(course::addBatch);
        }

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = getCourseById(id);

        if (!course.getName().equalsIgnoreCase(courseDTO.getName()) && courseRepository.existsByName(courseDTO.getName())) {
            throw new RuntimeException("Course name '" + courseDTO.getName() + "' already exists for another course.");
        }

        course.setName(courseDTO.getName());

        // Update instructors: Clear existing and re-add
        // Need to iterate and call removeInstructor to maintain bidirectional link
        new HashSet<>(course.getInstructors()).forEach(instructor -> instructor.removeCourse(course)); // Remove from inverse side
        course.getInstructors().clear(); // Clear the owning side's collection

        if (courseDTO.getInstructorIds() != null && !courseDTO.getInstructorIds().isEmpty()) {
            Set<Instructor> newInstructors = new HashSet<>(
                    instructorRepository.findAllById(courseDTO.getInstructorIds())
            );
            newInstructors.forEach(course::addInstructor); // Add new, maintains bidirectional link
        }

        // Update batches: Clear existing and re-add
        new HashSet<>(course.getBatches()).forEach(batch -> batch.removeCourse(course));
        course.getBatches().clear();
        if (courseDTO.getBatchIds() != null && !courseDTO.getBatchIds().isEmpty()) {
            Set<Batch> newBatches = new HashSet<>(
                    batchRepository.findAllById(courseDTO.getBatchIds())
            );
            newBatches.forEach(course::addBatch);
        }

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);

        // Remove bidirectional links before deleting the course
        new HashSet<>(course.getInstructors()).forEach(instructor -> instructor.removeCourse(course));
        course.getInstructors().clear(); // Clear the collection on the course side

        new HashSet<>(course.getBatches()).forEach(batch -> batch.removeCourse(course));
        course.getBatches().clear();

        courseRepository.delete(course);
    }

    @Override
    public boolean existsByName(String name) {
        return courseRepository.existsByName(name);
    }

    // NEW: Implementation for assigning instructors
    @Override
    @Transactional
    public void assignInstructorsToCourse(Long courseId, List<Long> instructorIds) {
        Course course = getCourseById(courseId); // Throws RuntimeException if not found

        // Remove all current instructors from the course AND their bidirectional link
        new HashSet<>(course.getInstructors()).forEach(instructor -> instructor.removeCourse(course));
        course.getInstructors().clear();

        // Add new instructors if provided
        if (instructorIds != null && !instructorIds.isEmpty()) {
            List<Instructor> newInstructors = instructorRepository.findAllById(instructorIds);
            // Add new instructors, maintaining bidirectional link
            newInstructors.forEach(course::addInstructor);
        }

        // Save the updated course. Because the relationship is managed by the owning side (Course),
        // saving the course will cascade changes to the join table.
        courseRepository.save(course);
    }
}

