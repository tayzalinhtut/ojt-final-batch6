package com.ojt.controller;

import com.ojt.dto.TimetableDTO;
import com.ojt.entity.Timetable;
import com.ojt.enumeration.DayOfWeek;
import com.ojt.service.CourseService;
import com.ojt.service.TimetableService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor/timetable")
public class InstructorTimetableController {

    private final TimetableService timetableService;
    private final CourseService courseService;

    @GetMapping("/available-times")
    @ResponseBody
    public List<String> getAvailableTimes(@RequestParam("day") String day) {
        System.out.println("Fetching times for: " + day); // <-- Add this for debugging
        DayOfWeek selectedDay = DayOfWeek.valueOf(day);
        return timetableService.getAvailableTimeSlots(selectedDay);
    }

    @GetMapping("/list")
    public String showAllTimetables(Model model) {
        List<Timetable> timetables = timetableService.findAll();
        model.addAttribute("timetables", timetables);
        return "instructor/timetable/list";
    }

    @GetMapping
    public String viewTimetable(Model model) {
        List<Timetable> timetables = timetableService.findAll();

        model.addAttribute("timetables", timetables);
        model.addAttribute("timeSlots", List.of(
                "8-9 AM", "9-10 AM", "10-11 AM", "11-12 AM",
                "12-1 PM", "1-2 PM", "2-3 PM", "3-4 PM", "4-5 PM"
        ));
        model.addAttribute("days", List.of(
                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"
        ));

        return "instructor/timetable";
    }

    @PostMapping("/list")
    public String createTimetable(@ModelAttribute TimetableDTO timetableDTO) {
        timetableService.save(timetableDTO);
        return "redirect:/instructor/timetable/list";
    }

}
