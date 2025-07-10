package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

}
