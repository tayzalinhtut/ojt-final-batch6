package com.ojt.service;

import com.ojt.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    double calculatedAttendancePercentage(Long ojtId, Long batchId);

    List<Attendance> getAllAttends();

    String getAttendanceStatus(Long ojtId, LocalDate date);

    List<Attendance> getPendingAttendance();
    void approveAttendance(Long id);
    void rejectAttendance(Long id);

    long countAttendancePending();
}

