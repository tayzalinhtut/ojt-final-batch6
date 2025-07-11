package com.ojt.controller;

import com.ojt.dto.EvaluationViewDTO;
import com.ojt.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/evaluation")
    public String showEvaluationPage(Model model) {
        List<EvaluationViewDTO> evaluations = evaluationService.getAllEvaluations();
        model.addAttribute("evaluations", evaluations);
        return "admin/evaluation/evaluation";
    }

    @GetMapping("/evaluation/{id}")
    public String showEvaluationDetail(@PathVariable Long id, Model model) {
        EvaluationViewDTO evaluation = evaluationService.getEvaluationDetailById(id);

        if (evaluation == null) {
            return "redirect:/admin/evaluation";
        }

        model.addAttribute("evaluation", evaluation);
        return "admin/evaluation/evaluation-detail";
    }
}
