package com.ojt.service;

import com.ojt.dto.TimetableDTO;
import com.ojt.entity.Timetable;
import com.ojt.enumeration.DayOfWeek;

import java.util.List;

public interface TimetableService {
    Timetable save(TimetableDTO dto);
    List<Timetable> findAll();
    Timetable update(Long id, TimetableDTO dto);
    void delete(Long id);
    Timetable findById(Long id);
    List<String> getAvailableTimeSlots(DayOfWeek dayOfWeek);

}
