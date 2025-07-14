package com.ojt.controller;

import com.ojt.dto.ChartDataDTO;
import com.ojt.entity.Evaluation;
import com.ojt.repository.CourseRepository;
import com.ojt.repository.EvaluationRepository;
import com.ojt.repository.OJTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InstructorDashboardController {

    @Autowired
    private OJTRepository ojtRepository;

   /* @Autowired
    private UserAccRepository userAccRepository;*/

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    //private final UserRepository userAccService;

    /*@Autowired
    public InstructorDashboardController(UserAccServiceImpl userAccService) {
        this.userAccService = userAccService;
    }*/

    // ✅ Instructor Dashboard View
    @GetMapping("/instructor/instructor-dashboard")
    public String showDashboard(Model model) {
        long ojtCount = ojtRepository.count();
        model.addAttribute("ojtCount", ojtCount);

        // ✅ Current Date (e.g., July 10, 2025)
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        model.addAttribute("currentDate", formattedDate);

        return "instructor/instructor-dashboard";
    }

    // ✅ Profile Page
    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }

    // ✅ Reset Password Page
    @GetMapping("/user/resetPassword")
    public String showResetPassword() {
        return "user/resetPassword";
    }

    // ✅ Change Password Page
    @GetMapping("/user/change-password")
    public String showChangePassword() {
        return "user/change-password";
    }

    // ✅ Chart Data API for Dashboard
    @GetMapping("/api/chart-data")
    @ResponseBody
    public ChartDataDTO getChartData() {
        ChartDataDTO dto = new ChartDataDTO();

        dto.setLabels(List.of(
                "Teamwork", "Leadership", "RD/Assignment", "Research & Tech",
                "Logical Thinking", "Error Handling", "Accuracy",
                "Coding Standard", "Assignment Complete."
        ));

        List<ChartDataDTO.ChartDataset> datasets = new ArrayList<>();

        List<Evaluation> evaluations = evaluationRepository.findAll();
        for (Evaluation e : evaluations) {
            String studentName = e.getOjt() != null && e.getOjt().getCv() != null
                    ? e.getOjt().getCv().getName()
                    : "Unknown";

            List<Integer> data = List.of(
                    e.getTeamwork(),
                    e.getLeadership(),
                    e.getAssignmentUnderstanding(),
                    e.getTechnicalSkill(),
                    e.getLogicalThinking(),
                    e.getErrorHandling(),
                    e.getAccuracy(),
                    e.getStandardOrFormatting(),
                    e.getAssignmentCompetence()
            );

            ChartDataDTO.ChartDataset ds = new ChartDataDTO.ChartDataset();
            ds.setLabel(studentName);
            ds.setData(data);
            ds.setBackgroundColor(generateRandomRGBA());

            datasets.add(ds);
        }

        dto.setDatasets(datasets);
        return dto;
    }

    // ✅ Generates a random RGBA color for chart bars
    private String generateRandomRGBA() {
        int r = (int) (Math.random() * 200);
        int g = (int) (Math.random() * 200);
        int b = (int) (Math.random() * 200);
        return String.format("rgba(%d, %d, %d, 0.7)", r, g, b);
    }
}

