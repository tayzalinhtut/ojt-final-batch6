package com.ojt.controller;

import com.ojt.repository.AttendanceRepository;
import com.ojt.service.AttendanceService;
import com.ojt.service.BatchService;
import com.ojt.service.OJTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {
    @Autowired
    private OJTService ojtService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private BatchService batchService;

    @GetMapping
    public String dashboard(Model model) {

        long activeStudentCount = ojtService.getOjtStatusCount();
        long attendancePending = attendanceService.countAttendancePending();
        long batchTotal = batchService.countTotalBatches();

        model.addAttribute("activeStudentCount", activeStudentCount);
        model.addAttribute("attendancePending", attendancePending);
        model.addAttribute("batchTotal", batchTotal);
        return "admin/dashboard/dashboard";
    }

}
