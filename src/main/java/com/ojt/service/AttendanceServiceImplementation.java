package com.ojt.service;

import com.ojt.entity.Attendance;
import com.ojt.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttendanceServiceImplementation implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public List<Attendance> getPendingAttendance() {
        return attendanceRepository.findByActionFalse();
    }

    @Override
    public void approveAttendance(Long id) {
        attendanceRepository.findById(id).ifPresent(a -> {
            a.setAction(true);
            attendanceRepository.save(a);
        });
    }

    @Override
    public void rejectAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}

