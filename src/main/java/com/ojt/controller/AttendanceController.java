package com.ojt.controller;

import com.ojt.service.AttendanceService;
import com.ojt.entity.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("")
    public String attendance(Model model) {
        List<Attendance> attendanceList = attendanceService.getPendingAttendance();
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("activePage", "attendance");
        return "admin/attendance/submit-student-attendance";
    }

    @PostMapping("/approve")
    public String approve(@RequestParam Long id) {
        attendanceService.approveAttendance(id);
        return "redirect:/admin/attendance";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam Long id) {
        attendanceService.rejectAttendance(id);
        return "redirect:/admin/attendance";
    }
}

