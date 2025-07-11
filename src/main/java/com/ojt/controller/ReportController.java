package com.ojt.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CVRepository;
import com.ojt.service.ReportService;

@Controller
@RequestMapping("/admin")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private BatchRepository batchRepo;

    @Autowired
    private CVRepository cvRepository;

    @GetMapping("/report")
    public String showReport(Model model) {
        List<Batch> batchData = batchRepo.findAll();
        List<CV> cvData = cvRepository.findAll();
        Set<Integer> cvYears = new TreeSet<>();
        Map<Long, String> batchLabels = new LinkedHashMap<>();

        for (Batch b : batchData) {
            batchLabels.put(b.getId(), b.getName());
        }

        model.addAttribute("batchLabels", batchLabels);

        for (CV cv : cvData) {
            cvYears.add(cv.getCreatedAt().getYear());
        }

        model.addAttribute("cvYears", cvYears);
        model.addAttribute("batchLabels", batchLabels);

        return "admin/charts/report";
    }

}