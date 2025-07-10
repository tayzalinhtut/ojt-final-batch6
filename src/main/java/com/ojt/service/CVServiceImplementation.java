package com.ojt.service;

import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CVServiceImplementation implements CVService {
    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private BatchRepository batchRepository;

//    @Override
//    public long acceptedStudentCount(Long batchId) {
//        return cvRepository.countByBatchIdAndStatus_StatusType(batchId, StatusType.Offer_Accept);
//    }
//
//    @Override
//    public long withDrawStudentCount(Long batchId) {
//        return cvRepository.countByBatchIdAndStatus_StatusType(batchId, StatusType.OJT_Withdraw);
//    }
//
//
//
//    @Override
//    public long getOjtStatusStudent(Long batchId) {
//        return cvRepository.countByBatchIdAndStatus_StatusTypeAndStatus_StatusType(batchId, StatusType.Offer_Accept, StatusType.OJT_Withdraw);
//    }

    @Override
    public List<CV> getOjtByBatchId(Long batchId) {
//        return cvRepository.findByBatchIdAndStatus_StatusType(batchId, StatusType.Offer_Accept);
        return cvRepository.findByBatchId(batchId);
    }

//    @Override
//    public List<CV> getOjtByBatchIdAndStatusTypeAndStatus(Long batchId) {
//        List<StatusType> types = List.of(
//                StatusType.Offer_Accept,
//                StatusType.OJT_Withdraw,
//                StatusType.OJT_Pass,
//                StatusType.OJT_Fail
//        );
//        return cvRepository.findByBatchIdAndStatusIn(batchId, types);
//    }



//    @Override
//    public List<CV> getOjtWithoutBatch() {
//        return cvRepository.findByBatchIsNull();
//    }


    @Override
    public void assignStudentsToBatch(Long batchId, List<Long> studentIds) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch ID: " + batchId));

        List<CV> cvs = cvRepository.findAllById(studentIds);
        for (CV cv : cvs) {
            cv.setBatch(batch);
        }
        cvRepository.saveAll(cvs);
    }

    @Override
    public void removeStudentsFromBatch(List<Long> studentIds) {
        List<CV> ojts = cvRepository.findAllById(studentIds);
        for (CV cv : ojts) {
            cv.setBatch(null);
        }
        cvRepository.saveAll(ojts);
    }
}
