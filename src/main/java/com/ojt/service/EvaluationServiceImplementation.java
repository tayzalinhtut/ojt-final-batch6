package com.ojt.service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.ojt.dto.EvaluatorBreakdownDTO;
import com.ojt.dto.EvaluationViewDTO;
import com.ojt.entity.Course;
import com.ojt.entity.Instructor;
import com.ojt.entity.OJT;
import com.ojt.repository.CourseRepository;
import com.ojt.repository.InstructorRepository;
import com.ojt.repository.OJTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ojt.entity.Evaluation;
import com.ojt.repository.EvaluationRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluationServiceImplementation implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Htet Linn Aung
    @Override
    public List<EvaluationViewDTO> getAllEvaluations() {
        Map<Long, List<Evaluation>> groupedByOjt = evaluationRepository.findAll().stream()
                .collect(Collectors.groupingBy(e -> e.getOjt().getId()));

        List<EvaluationViewDTO> dtoList = new ArrayList<>();

        for (Map.Entry<Long, List<Evaluation>> entry : groupedByOjt.entrySet()) {
            List<Evaluation> evaluations = entry.getValue();
            if (evaluations.isEmpty()) continue;

            Evaluation first = evaluations.get(0);
            EvaluationViewDTO dto = new EvaluationViewDTO();

            dto.setId(first.getId());
            dto.setStudentName(first.getOjt().getCv().getName());
            dto.setStudentCode(first.getOjt().getOjtId());
            dto.setBatchName(first.getOjt().getCv().getBatch().getName());

            int totalScore = evaluations.stream().mapToInt(this::calculateTotalScore).sum();
            dto.setOverallScore(totalScore);

            Set<String> evaluatorNames = evaluations.stream()
                    .map(Evaluation::getInstructor)
                    .filter(Objects::nonNull)
                    .map(Instructor::getName)
                    .collect(Collectors.toSet());
            dto.setEvaluators(String.join(", ", evaluatorNames));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public EvaluationViewDTO getEvaluationDetailById(Long id) {
        Optional<Evaluation> optionalEvaluation = evaluationRepository.findById(id);
        if (optionalEvaluation.isEmpty()) {
            return null;
        }

        Evaluation sampleEval = optionalEvaluation.get();
        Long ojtId = sampleEval.getOjt().getId();

        List<Evaluation> evaluations = evaluationRepository.findAll().stream()
                .filter(e -> e.getOjt().getId().equals(ojtId))
                .collect(Collectors.toList());

        Evaluation first = evaluations.get(0);
        EvaluationViewDTO dto = new EvaluationViewDTO();

        dto.setId(first.getId());
        dto.setStudentName(first.getOjt().getCv().getName());
        dto.setStudentCode(first.getOjt().getOjtId());
        dto.setBatchName(first.getOjt().getCv().getBatch().getName());
        dto.setStartDate(first.getOjt().getCv().getBatch().getStartDate().format(formatter));
        dto.setEndDate(first.getOjt().getCv().getBatch().getEndDate().format(formatter));

        int totalScore = evaluations.stream().mapToInt(this::calculateTotalScore).sum();
        dto.setOverallScore(totalScore);
        dto.setGrading(getGrading(totalScore));

        Set<String> allEvaluators = evaluations.stream()
                .map(Evaluation::getInstructor)
                .filter(Objects::nonNull)
                .map(Instructor::getName)
                .collect(Collectors.toSet());
        dto.setEvaluators(String.join(", ", allEvaluators));

        List<EvaluatorBreakdownDTO> breakdowns = new ArrayList<>();
        for (Evaluation e : evaluations) {
            Instructor instructor = e.getInstructor();
            if (instructor != null) {
                EvaluatorBreakdownDTO eb = new EvaluatorBreakdownDTO();
                eb.setInstructorName(instructor.getName());

                String courseName = instructor.getCourses().isEmpty() ? "N/A"
                        : instructor.getCourses().stream().map(Course::getName).collect(Collectors.joining(", "));
                eb.setCourseName(courseName);

                eb.setEvaluationDate(e.getCreatedDate() != null ? e.getCreatedDate().format(formatter) : "N/A");
                eb.setNote(e.getNote());

                eb.setTeamwork(e.getTeamwork());
                eb.setLeadership(e.getLeadership());
                eb.setAssignmentUnderstanding(e.getAssignmentUnderstanding());
                eb.setTechnicalSkill(e.getTechnicalSkill());
                eb.setLogicalThinking(e.getLogicalThinking());
                eb.setErrorHandling(e.getErrorHandling());
                eb.setAccuracy(e.getAccuracy());
                eb.setStandardOrFormatting(e.getStandardOrFormatting());
                eb.setAssignmentCompetence(e.getAssignmentCompetence());

                breakdowns.add(eb);
            }
        }

        dto.setEvaluatorBreakdowns(breakdowns);
        return dto;
    }

    private int calculateTotalScore(Evaluation e) {
        return e.getTeamwork() + e.getLeadership() + e.getAssignmentUnderstanding() + e.getTechnicalSkill()
                + e.getLogicalThinking() + e.getErrorHandling() + e.getAccuracy()
                + e.getStandardOrFormatting() + e.getAssignmentCompetence();
    }

    private String getGrading(int score) {
        if (score >= 200) return "A";
        if (score >= 150) return "B";
        return "C";
    }

    // Thant Sin Win
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

    // Htet Wai Yan Soe
    @Autowired
    private OJTRepository ojtRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public List<Evaluation> findALlEvaluation() {
        // TODO Auto-generated method stub
        return evaluationRepository.findAll();
    }

    @Override
    public Optional<Evaluation> findEvaluationById(Long id) {
        // TODO Auto-generated method stub
        return evaluationRepository.findById(id);
    }

    @Override
    public Evaluation saveEvaluation(Evaluation evaluation) {
        // TODO Auto-generated method stub
        return evaluationRepository.save(evaluation);
    }

    @Override
    public void deleteEvaluationById(Long id) {
        // TODO Auto-generated method stub
        evaluationRepository.deleteById(id);
    }

    @Override
    public Evaluation createUpdateEvaluation(Evaluation evaluationDetails, Long ojtId, Long courseId,
                                             Long instructorId) {
        OJT ojt = ojtRepository.findById(ojtId).orElseThrow(() -> new RuntimeException("OJT Student not found with id:" + ojtId));
        Course courses = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("OJT Student not found with id:" + courseId));
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("OJT Student not found with id:" + instructorId));


        // ✅ Prevent duplicate evaluation for same student and course
        Optional<Evaluation> existingEval = evaluationRepository.findByOjtIdAndCourseId(ojtId, courseId);
        if (existingEval.isPresent()) {
            if (evaluationDetails.getId() == null || !existingEval.get().getId().equals(evaluationDetails.getId())) {
                throw new IllegalArgumentException("This student has already been evaluated for the selected course.");
            }
        }
        Evaluation evaluation;
        if (evaluationDetails.getId() != null) {
            // This is an update operation (used by edit form)
            evaluation = evaluationRepository.findById(evaluationDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Evaluation not found with id: " + evaluationDetails.getId()));
            // Update fields from evaluationDetails to the fetched evaluation
            evaluation.setTeamwork(evaluationDetails.getTeamwork());
            evaluation.setLeadership(evaluationDetails.getLeadership());
            evaluation.setAssignmentUnderstanding(evaluationDetails.getAssignmentUnderstanding());
            evaluation.setTechnicalSkill(evaluationDetails.getTechnicalSkill());
            evaluation.setLogicalThinking(evaluationDetails.getLogicalThinking());
            evaluation.setErrorHandling(evaluationDetails.getErrorHandling());
            evaluation.setAccuracy(evaluationDetails.getAccuracy());
            evaluation.setStandardOrFormatting(evaluationDetails.getStandardOrFormatting());
            evaluation.setAssignmentCompetence(evaluationDetails.getAssignmentCompetence());
            evaluation.setNote(evaluationDetails.getNote());
        } else {
            // This is a create operation (used by create form)
            evaluation = evaluationDetails;
        }

        // Set relationships
        evaluation.setOjt(ojt);
        evaluation.setCourse(courses);
        evaluation.setInstructor(instructor);

        return evaluationRepository.save(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evaluation> findAllEvaluationPaginated(Pageable pageable) {
        return evaluationRepository.findAllWithStudentAndCourse(pageable);
    }//show Student Name and Course in instructor Dashboard


    @Override
    @Transactional(readOnly = true)
    public Page<Evaluation> searchEvalutionsByCvName(String studentName, Pageable pageable) {
        // TODO Auto-generated method stub
        return evaluationRepository.findByOjt_Cv_NameContainingIgnoreCase(studentName, pageable);
    }
}


