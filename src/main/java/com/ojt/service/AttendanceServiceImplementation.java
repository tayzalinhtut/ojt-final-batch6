package com.ojt.service;

import com.ojt.entity.Attendance;
import com.ojt.entity.Batch;
import com.ojt.enumeration.AttendType;
import com.ojt.repository.AttendanceRepository;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImplementation implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public double calculatedAttendancePercentage(Long ojtId, Long batchId) {
        Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"));

        List<LocalDate> holidays = holidayRepository.findAllHolidayDates();
        List<LocalDate> offDay = new ArrayList<LocalDate>();
        List<LocalDate> batchAllDates = batch.getStartDate().datesUntil(batch.getEndDate().plusDays(1))
                .collect(Collectors.toList());

        for(LocalDate d : batchAllDates) {
            if("Sun".contains(d.format(DateTimeFormatter.ofPattern("E")))) {
                offDay.add(d);
            }
        }

        // calculate working days
        List<LocalDate> workingDays = batch.getStartDate().datesUntil(batch.getEndDate().plusDays(1))
                .filter(date -> !holidays.contains(date) && !offDay.contains(date)).collect(Collectors.toList());

        Long calHoliday = batch.getStartDate().datesUntil(batch.getEndDate().plusDays(1))
                .filter(date -> holidays.contains(date)).count();

        long totalWorkingDays = workingDays.size();

        // get present days
        long presentDays = attendanceRepository.findByOjtIdAndAttendType(ojtId, AttendType.Attend).stream()
                .filter(a -> workingDays.contains(a.getDate())).count();

        // calculate percentage
        return totalWorkingDays > 0 ? (double) presentDays / totalWorkingDays * 100 : 0;
    }

    @Override
    public List<Attendance> getAllAttends() {
        // TODO Auto-generated method stub
        return attendanceRepository.findAll();
    }

    @Override
    public String getAttendanceStatus(Long ojtId, LocalDate date) {
        return attendanceRepository.findByOjtIdAndDate(ojtId, date)
                .map(attendance -> attendance.getAttendType().name()) // or .toString()
                .orElse("ABSENT"); // Default value if no record found
    }


    //Tay
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

    @Override
    public long countAttendancePending(){
        long count = attendanceRepository.countByActionFalse();
        if (count == 0) {
            return 0;
        }
        return count;
    }
}

