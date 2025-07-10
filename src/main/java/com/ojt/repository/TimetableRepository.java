package com.ojt.repository;

import com.ojt.entity.Timetable;
import com.ojt.enumeration.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByDayOfWeek(DayOfWeek dayOfWeek);
}
