package com.ojt.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataDTO {
    private List<String> labels;
    private List<ChartDataset> datasets;

    @Data   // Inner class for datasets
    public static class ChartDataset {
        private String label;
        private List<Integer> data;
        private String backgroundColor;

    }

}