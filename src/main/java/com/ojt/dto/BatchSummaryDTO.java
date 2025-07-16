package com.ojt.dto;

import lombok.Data;

@Data
public class BatchSummaryDTO {
    private Long id;
    private String name;
    private long studentCount;
    private long courseCount;
}