package com.ojt.service;

import com.ojt.entity.CV;
import com.ojt.entity.OJT;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.CVRepository;
import com.ojt.repository.OJTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OJTServiceImplementation implements OJTService {

    @Autowired
    private OJTRepository ojtRepository;

    @Autowired
    private CVRepository cvRepository;

    @Override
    public List<OJT> getOjtByBatchIdAndStatusTypeAndStatus(Long batchId) {
        List<StatusType> types = List.of(
                StatusType.OJT_Active,
                StatusType.OJT_Withdraw,
                StatusType.OJT_Pass,
                StatusType.OJT_Fail
        );
        return ojtRepository.findWithCvByBatchIdAndStatusTypeIn(batchId, types);
    }



    @Override
    public long countOjtActiveStudent(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Active);
    }


    @Override
    public long countWithDrawStudentCount(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Withdraw);
    }

    @Override
    public long countOfferAcceptedStudent(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.Offer_Accept);
    }

    @Override
    public long countOJTPassed(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Pass);
    }

    @Override
    public long countOJTFailed(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Fail);
    }

//    @Override
//    public long getOjtStatusStudent(Long batchId) {
//        // Optional: Combine OJT_Active + Withdraw, etc. Here is just one example:
//        long active = ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Active);
//        long withdraw = ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Withdraw);
//        return active + withdraw;
//    }

    @Override
    public long countOjtAllStudent(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.Offer_Accept);
    }

//    @Override
//    public List<OJT> getOjtByBatchId(Long batchId) {
//        return ojt.findByBatchId(batchId);
//    }
}
