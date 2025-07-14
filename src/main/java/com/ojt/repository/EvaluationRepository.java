package com.ojt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Evaluation;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByOjt_Id(Long ojtId);

    // Htet Wai Yan Soe
    @Query("Select e from Evaluation e JOIN FETCH e.ojt o JOIN FETCH o.cv JOIN FETCH  e.course ORDER BY e.id DESC ")
    Page<Evaluation> findAllWithStudentAndCourse(Pageable pageable);


    @Query("SELECT e FROM Evaluation e JOIN FETCH e.ojt o JOIN FETCH o.cv JOIN FETCH e.course WHERE LOWER(o.cv.name) LIKE LOWER(CONCAT('%', :studentName, '%')) ORDER BY e.id DESC")
    Page<Evaluation> findByOjt_Cv_NameContainingIgnoreCase(String studentName, Pageable pageable);
    Optional<Evaluation> findByOjtIdAndCourseId(Long ojtId, Long courseId);
}
