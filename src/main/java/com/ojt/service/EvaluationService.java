package com.ojt.service;

import com.ojt.dto.EvaluationViewDTO;

import java.util.List;
import java.util.Map;

public interface EvaluationService {
    Map<String, Integer> getSummedSkillsByOjt(Long ojtId);

    List<EvaluationViewDTO> getAllEvaluations();
    EvaluationViewDTO getEvaluationDetailById(Long id);
}
