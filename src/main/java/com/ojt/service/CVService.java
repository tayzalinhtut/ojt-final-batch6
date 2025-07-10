package com.ojt.service;

import com.ojt.entity.CV;
import com.ojt.enumeration.StatusType;

import java.util.List;

public interface CVService {
//    List<CV> getOjtWithoutBatch();
    List<CV> getOjtByBatchId(Long batchId);
//    List<CV> getOjtByBatchIdAndStatusTypeAndStatus(Long batchId);
    void assignStudentsToBatch(Long batchId, List<Long> studentIds);
    void removeStudentsFromBatch(List<Long> studentIds);
//    long acceptedStudentCount(Long batchId);
//    long withDrawStudentCount(Long batchId);
//    long getOjtStatusStudent(Long batchId);
}
