package com.ojt.controller;

import com.ojt.entity.Notification;
import com.ojt.repository.AttendanceRepository;
import com.ojt.service.AttendanceService;
import com.ojt.service.BatchService;
import com.ojt.service.NotificationService;
import com.ojt.service.OJTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {
    @Autowired
    private OJTService ojtService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private BatchService batchService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String dashboard(Model model, Principal principal) {

        long activeStudentCount = ojtService.getOjtStatusCount();
        long attendancePending = attendanceService.countAttendancePending();
        long batchTotal = batchService.countTotalBatches();
        List<Notification> notifications = notificationService.getNotifications();

        model.addAttribute("activeStudentCount", activeStudentCount);
        model.addAttribute("attendancePending", attendancePending);
        model.addAttribute("batchTotal", batchTotal);
        model.addAttribute("notifications", notifications);
        model.addAttribute("currentUser", "admin@gmail.com");

        return "admin/dashboard/dashboard";
    }

}
