package com.ojt.repository;

import com.ojt.enumeration.AttendType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ojt.entity.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByActionFalse();
    long countByActionFalse();
    // Find attendance for specific student on specific date
    Optional<Attendance> findByOjtIdAndDate(Long ojtId, LocalDate date);


    List<Attendance> findByOjtIdAndAttendType(Long ojtId, AttendType attend);
}
