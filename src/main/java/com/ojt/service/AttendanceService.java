package com.ojt.service;

import com.ojt.entity.Attendance;
import java.util.List;

public interface AttendanceService {
    List<Attendance> getPendingAttendance();
    void approveAttendance(Long id);
    void rejectAttendance(Long id);
}

