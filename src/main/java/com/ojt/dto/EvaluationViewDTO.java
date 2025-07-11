package com.ojt.dto;

import lombok.Data;

import java.util.List;

@Data
public class EvaluationViewDTO {

    // Fields for list view
    private Long id;
    private String studentName;
    private String studentCode;
    private String batchName;
    private int overallScore;
    private String evaluators;

    // Fields for detail view
    private String grading;
    private String startDate;
    private String endDate;
    private List<EvaluatorBreakdown> evaluatorBreakdowns;

    @Data
    public static class EvaluatorBreakdown {
        private String instructorName;
        private String courseName;
        private String note;
        private String evaluationDate;
        private int teamwork;
        private int leadership;
        private int assignmentUnderstanding;
        private int technicalSkill;
        private int logicalThinking;
        private int errorHandling;
        private int accuracy;
        private int standardOrFormatting;
        private int assignmentCompetence;
    }

}
