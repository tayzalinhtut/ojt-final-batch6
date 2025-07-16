package com.ojt.repository;

import com.ojt.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Courses, Long> {
    List<Courses> findByBatches_Id(Long batchId);
    @Query("SELECT c FROM Courses c WHERE c.batches IS EMPTY OR :batchId NOT IN (SELECT b.id FROM c.batches b)") //Tay Za Lin Htut
    List<Courses> findCoursesNotInBatchOrUnassigned(@Param("batchId") Long batchId);

    @Query("SELECT c FROM Courses c LEFT JOIN FETCH c.instructors WHERE c.id = :id") //Nyi Min Htet Lwin
    Optional<Courses> findByIdWithInstructors(Long id);

    @Query("SELECT DISTINCT c FROM Courses c LEFT JOIN FETCH c.instructors") //Nyi Min Htet Lwin
    List<Courses> findAllWithInstructors();

    // This method is correctly defined and Spring Data JPA will implement it automatically
    boolean existsByName(String name); //Nyi Min Htet Lwin

}

