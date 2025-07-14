package com.ojt.service;

import com.ojt.dto.EvaluationViewDTO;
import com.ojt.entity.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EvaluationService {
    // Htet Linn Aung
    Map<String, Integer> getSummedSkillsByOjt(Long ojtId);

    List<EvaluationViewDTO> getAllEvaluations();

    EvaluationViewDTO getEvaluationDetailById(Long id);

    // Htet Wai Yan Soe
    List<Evaluation> findALlEvaluation();

    Optional<Evaluation> findEvaluationById(Long id);

    Evaluation saveEvaluation(Evaluation evaluation);

    void deleteEvaluationById(Long id);

    Evaluation createUpdateEvaluation(Evaluation evaluationDetails, Long ojtId, Long courseId, Long instructorId);

    Page<Evaluation> findAllEvaluationPaginated(Pageable pageable);

    Page<Evaluation> searchEvalutionsByCvName(String studentName, Pageable pageable);
}
