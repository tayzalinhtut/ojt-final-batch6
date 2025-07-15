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
    private List<EvaluatorBreakdownDTO> evaluatorBreakdowns;

}
