package com.ojt.service;

import com.ojt.entity.CV;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
    public List<CV> getCvPerYear(int year);
    public List<CV> getCvPerBatch(Long batchId);
    public int calculateCvPerYear(int year, Long batchId);
    public int calculateCvScan(int year, Long batchId, Boolean isDropoffChart);
    public int calculateCodeTest(int year, Long batchId, Boolean isDropoffChart);
    public int calculateInterview(int year, Long batchId, Boolean isDropoffChart);
    public int calculateOffer(int year, Long batchId, Boolean isDropoffChart);
    public Map<String, String> getMonthsBetween(LocalDate startDate, LocalDate endDate);
}
