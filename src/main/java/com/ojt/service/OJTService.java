package com.ojt.service;

import com.ojt.entity.CV;
import com.ojt.entity.OJT;

import java.util.List;

public interface OJTService {

    List<OJT> getOjtByBatchIdAndStatusTypeAndStatus(Long batchId);

    long countOjtActiveStudent(Long batchId);

    long countWithDrawStudentCount(Long batchId);

    long countOfferAcceptedStudent(Long batchId);

    long countOJTPassed(Long batchId);

    long countOJTFailed(Long batchId);

//    long getOjtStatusStudent(Long batchId);

    long countOjtAllStudent(Long batchId);
//    List<OJT> getOjtByBatchId(Long batchId);


}

