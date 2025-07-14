package com.ojt.controller;

import com.ojt.dto.TimetableDTO;
import com.ojt.entity.Timetable;
import com.ojt.enumeration.DayOfWeek;
import com.ojt.service.CourseService;
import com.ojt.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/timetable")
public class TimetableController {

    private final TimetableService timetableService;
    private final CourseService courseService;

    @GetMapping
    public String viewTimetable(Model model) {
        List<Timetable> timetables = timetableService.findAll();

        model.addAttribute("timetables", timetables);
        model.addAttribute("timeSlots", List.of(
                "8-9 AM", "9-10 AM", "10-11 AM", "11-12 AM",
                "12-1 PM", "1-2 PM", "2-3 PM", "3-4 PM", "4-5 PM"
        ));
        model.addAttribute("days", List.of(
                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"
        ));

        return "admin/timetable/timetable";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("timetableDTO", new TimetableDTO());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("timetables", timetableService.findAll()); // ဒီလိုထည့်
        return "admin/timetable/create";
    }



    @PostMapping("/new")
    public String createTimetable(
            @Valid @ModelAttribute("timetableDTO") TimetableDTO timetableDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAll());
            return "admin/timetable/create";
        }

        timetableService.save(timetableDTO);
        return "redirect:/admin/timetable";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Timetable timetable = timetableService.findById(id);

        TimetableDTO dto = new TimetableDTO();
        dto.setId(timetable.getId());
        dto.setTime(timetable.getTime());
        dto.setDayOfWeek(timetable.getDayOfWeek());
        dto.setCourseId(timetable.getCourses().getId());

        model.addAttribute("timetableDTO", dto);
        model.addAttribute("courses", courseService.findAll());
        return "admin/timetable/edit";
    }

    @PostMapping("/update/{id}")
    public String updateTimetable(
            @PathVariable Long id,
            @Valid @ModelAttribute("timetableDTO") TimetableDTO dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAll());
            return "admin/timetable/edit";
        }

        timetableService.update(id, dto);
        return "redirect:/admin/timetable";
    }

    @GetMapping("/delete/{id}")
    public String deleteTimetable(@PathVariable Long id) {
        timetableService.delete(id);
        return "redirect:/admin/timetable";
    }
}
