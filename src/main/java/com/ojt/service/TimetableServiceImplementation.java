package com.ojt.service;

import com.ojt.dto.TimetableDTO;
import com.ojt.entity.Course;
import com.ojt.entity.Timetable;
import com.ojt.enumeration.DayOfWeek;
import com.ojt.repository.CourseRepository;
import com.ojt.repository.TimetableRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public
class TimetableServiceImplementation implements TimetableService {

    private final TimetableRepository timetableRepo;
    private final CourseRepository courseRepo;

    @Override
    public List<String> getAvailableTimeSlots(DayOfWeek dayOfWeek) {
        List<String> allSlots = List.of(
                "8-9 AM", "9-10 AM", "10-11 AM", "11-12 AM",
                "12-1 PM", "1-2 PM", "2-3 PM", "3-4 PM", "4-5 PM"
        );

        List<Timetable> dayEntries = timetableRepo.findByDayOfWeek(dayOfWeek);

        List<String> usedSlots = dayEntries.stream()
                .map(Timetable::getTime)
                .toList();

        return allSlots.stream()
                .filter(slot -> !usedSlots.contains(slot))
                .toList();
    }



    @Override
    public Timetable save(TimetableDTO dto) {
        Timetable tt = new Timetable();
        tt.setTime(dto.getTime());
        tt.setDayOfWeek(dto.getDayOfWeek());
        tt.setCourses(courseRepo.findById(dto.getCourseId()).orElseThrow());
        return timetableRepo.save(tt);
    }

    @Override
    public List<Timetable> findAll() {
        return timetableRepo.findAll();
    }

    @Override
    public Timetable update(Long id, TimetableDTO dto) {
        Timetable tt = timetableRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Timetable not found"));
        tt.setTime(dto.getTime());
        tt.setTime(dto.getTime());
        tt.setDayOfWeek(dto.getDayOfWeek());
        tt.setCourses(courseRepo.findById(dto.getCourseId()).orElseThrow());
        return timetableRepo.save(tt);
    }

    @Override
    public void delete(Long id) {
        timetableRepo.deleteById(id);
    }

    @Override
    public Timetable findById(Long id) {
        return timetableRepo.findById(id).orElseThrow();
    }
}
