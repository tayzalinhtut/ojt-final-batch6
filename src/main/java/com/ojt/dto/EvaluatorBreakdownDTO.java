package com.ojt.dto;

import lombok.Data;

@Data
public class EvaluatorBreakdownDTO {

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
