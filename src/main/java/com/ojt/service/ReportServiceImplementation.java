package com.ojt.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.entity.Holiday;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CVRepository;
import com.ojt.repository.HolidayRepository;

@Service
public class ReportServiceImplementation implements ReportService {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private BatchRepository batchRepository;

    public List<CV> getCvPerYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        List<CV> cvs = cvRepository.findByYear(start, end);

        return cvs;
    }

    public List<CV> getCvPerBatch(Long batchId) {
        Optional<Batch> optional = batchRepository.findById(batchId);
        Batch batch = null;
        if (optional.isPresent()) {
            batch = optional.get();
        } else {
            throw new RuntimeException("User not found for id: " + batchId);
        }

        List<CV> cvs = cvRepository.findByBatch(batch);

        return cvs;
    }

    public int calculateCvPerYear(int year, Long batchId) {
        List<CV> cvs = null;
        if (year > 0) {
            cvs = getCvPerYear(year);
        } else {
            cvs = getCvPerBatch(batchId);
        }

        return cvs.size();
    }

    public int calculateCvScan(int year, Long batchId, Boolean isDropoffChart) {
        List<CV> totalCv;
        int scanCv = 0;

        if (year > 0) {
            totalCv = getCvPerYear(year);
        } else {
            totalCv = getCvPerBatch(batchId);
        }

        if (totalCv.size() > 0) {
            for (CV cv : totalCv) {
                if(!isDropoffChart) {
                    if (cv.getStatus().getStatusType().equals(StatusType.Scan_Pass)) {
                        scanCv++;
                    }
                } else {
                    if (cv.getStatus().getStatusType().equals(StatusType.Scan_Fail)) {
                        scanCv++;
                    }
                }
            }
        }

        System.out.println("Scan CV: " + scanCv);

        return scanCv;
    }

    public int calculateCodeTest(int year, Long batchId, Boolean isDropoffChart) {
        List<CV> totalCv;
        int codeTestCv = 0;

        if (year > 0) {
            totalCv = getCvPerYear(year);
        } else {
            totalCv = getCvPerBatch(batchId);
        }

        if (totalCv.size() > 0) {
            for (CV cv : totalCv) {
                if(!isDropoffChart) {
                    if (cv.getStatus().getStatusType().equals(StatusType.CodeTest_Pass)) {
                        codeTestCv++;
                    }
                } else {
                    if (cv.getStatus().getStatusType().equals(StatusType.CodeTest_Fail)) {
                        codeTestCv++;
                    }
                }
            }
        }

        System.out.println("Code Test CV: " + codeTestCv);

        return codeTestCv;
    }

    public int calculateInterview(int year, Long batchId, Boolean isDropoffChart) {
        List<CV> totalCv;
        int interviewCv = 0;

        if (year > 0) {
            totalCv = getCvPerYear(year);
        } else {
            totalCv = getCvPerBatch(batchId);
        }

        if (totalCv.size() > 0) {
            for (CV cv : totalCv) {
                if(!isDropoffChart) {
                    if (cv.getStatus().getStatusType().equals(StatusType.Interview_Pass)) {
                        interviewCv++;
                    }
                } else {
                    if (cv.getStatus().getStatusType().equals(StatusType.Interview_Fail)) {
                        interviewCv++;
                    }
                }
            }
        }

        System.out.println("Interview CV: " + interviewCv);

        return interviewCv;
    }

    public int calculateOffer(int year, Long batchId, Boolean isDropoffChart) {
        List<CV> totalCv;
        int offerCv = 0;

        if (year > 0) {
            totalCv = getCvPerYear(year);
        } else {
            totalCv = getCvPerBatch(batchId);
        }

        if (totalCv.size() > 0) {
            for (CV cv : totalCv) {
                if(!isDropoffChart) {
                    if (cv.getStatus().getStatusType().equals(StatusType.Offer_Accept)) {
                        offerCv++;
                    }
                } else {
                    if (cv.getStatus().getStatusType().equals(StatusType.Offer_Reject)) {
                        offerCv++;
                    }
                }
            }
        }

        System.out.println("Offer CV: " + offerCv);

        return offerCv;
    }

    public Map<String, String> getMonthsBetween(LocalDate startDate, LocalDate endDate) {
        Map<String, String> months = new LinkedHashMap<String, String>();
        YearMonth currentMonth = YearMonth.from(startDate);
        YearMonth lastMonth = YearMonth.from(endDate);

        while (!currentMonth.isAfter(lastMonth)) {
            months.put(currentMonth.toString(), currentMonth.getMonth().toString());
            currentMonth = currentMonth.plusMonths(1);
        }
        System.out.println(months);
        return months;
    }
}