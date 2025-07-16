package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Instructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Query("SELECT i FROM Instructor i WHERE i.staffInfo.user.name = :name")
    Optional<Instructor> findByName(@Param("name") String name);

    @Query("SELECT i FROM Instructor i WHERE i.staffInfo.user.email = :email")
    Optional<Instructor> findByEmail(@Param("email") String email);

    // Htet Wai Yan Soe
    @Query("SELECT i FROM Instructor i WHERE i.staffInfo.staffId = :staffId")
    Instructor findByStaffId(@Param("staffId") Long staffId);//Optional<instrucotr>
}
