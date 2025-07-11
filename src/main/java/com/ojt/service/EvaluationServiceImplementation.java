package com.ojt.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ojt.entity.Evaluation;
import com.ojt.repository.EvaluationRepository;

@Service
public class EvaluationServiceImplementation implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Map<String, Integer> getSummedSkillsByOjt(Long ojtId) {
        List<Evaluation> evaluations = evaluationRepository.findByOjt_Id(ojtId);
        Map<String, Integer> skillTotals = new LinkedHashMap<>();

        // Initialize all skills to 0
        skillTotals.put("teamwork", 0);
        skillTotals.put("leadership", 0);
        skillTotals.put("assignmentUnderstanding", 0);
        skillTotals.put("technicalSkill", 0);
        skillTotals.put("logicalThinking", 0);
        skillTotals.put("errorHandling", 0);
        skillTotals.put("accuracy", 0);
        skillTotals.put("standardOrFormatting", 0);
        skillTotals.put("assignmentCompetence", 0);

        // Sum all evaluations for each skill
        for (Evaluation eval : evaluations) {
            if (eval.getOjt().getId() == ojtId) {
                skillTotals.put("teamwork", skillTotals.get("teamwork") + eval.getTeamwork());
                skillTotals.put("leadership", skillTotals.get("leadership") + eval.getLeadership());
                skillTotals.put("assignmentUnderstanding",
                        skillTotals.get("assignmentUnderstanding") + eval.getAssignmentUnderstanding());
                skillTotals.put("technicalSkill", skillTotals.get("technicalSkill") + eval.getTechnicalSkill());
                skillTotals.put("logicalThinking", skillTotals.get("logicalThinking") + eval.getLogicalThinking());
                skillTotals.put("errorHandling", skillTotals.get("errorHandling") + eval.getErrorHandling());
                skillTotals.put("accuracy", skillTotals.get("accuracy") + eval.getAccuracy());
                skillTotals.put("standardOrFormatting",
                        skillTotals.get("standardOrFormatting") + eval.getStandardOrFormatting());
                skillTotals.put("assignmentCompetence",
                        skillTotals.get("assignmentCompetence") + eval.getAssignmentCompetence());
            }
        }

        return skillTotals;
    }

}