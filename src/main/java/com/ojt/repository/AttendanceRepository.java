package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ojt.entity.Attendance;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByActionFalse();
}
