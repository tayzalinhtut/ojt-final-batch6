package com.ojt.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomBatchDataDTO {
    private String name;
    private String startDate;
    private String endDate;
    private List<CustomBatchDataMonthDTO> months;
    private List<CustomBatchDataStudentDTO> students;
}