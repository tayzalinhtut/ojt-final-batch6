package com.ojt.service;

import com.ojt.dto.OJTDTO;
import com.ojt.entity.CV;
import com.ojt.entity.OJT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OJTService {

    List<OJT> getOjtByBatchIdAndStatusTypeAndStatus(Long batchId);

    long getOjtStatusCount();

    long countOjtActiveStudent(Long batchId);

    long countWithDrawStudentCount(Long batchId);

    long countOJTPassed(Long batchId);

    long countOJTFailed(Long batchId);

//    long getOjtStatusStudent(Long batchId);

    long countOjtAllStudent(Long batchId);

    List<OJT> getAllOJT();

    List<OJT> getOJTByBatch(Long batchId);

    Page<OJT> getAllOJTWithoutAttendance(Pageable pageable);

    OJT getOJTById(Long id);

    OJT saveOJT(OJTDTO ojtDto);

    OJT updateOJT(Long id, OJTDTO ojtDto);

    void deleteOJT(Long id) throws Exception;

    List<OJT> getPassedOjtsByIds();

    // Htet Wai Yan Soe
    List<OJT> getOJTByStatus();
//    List<OJT> getOjtByBatchId(Long batchId);


}

