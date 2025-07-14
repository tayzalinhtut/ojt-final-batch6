package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Instructor;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    // Htet Wai Yan Soe
    Instructor findByEmail(String email);
    Instructor findByStaffId(Long staffId);//Optional<instrucotr>
    boolean existsByEmail(String email);
    Optional<Instructor> findByName(String name);
}
