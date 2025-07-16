package com.ojt.service;

import com.ojt.dto.OJTDTO;
import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.entity.OJT;
import com.ojt.entity.Status;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CVRepository;
import com.ojt.repository.OJTRepository;
import com.ojt.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OJTServiceImplementation implements OJTService {
    //Tay Za Lin Htut
    @Autowired
    private OJTRepository ojtRepository;

    @Autowired
    private SystemUserService systemUserService;

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
    public long getOjtStatusCount() {
        return ojtRepository.countByStatusTypeOrStatusType(StatusType.OJT_Active, StatusType.OJT_Pass);
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
    public long countOJTPassed(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Pass);
    }

    @Override
    public long countOJTFailed(Long batchId) {
        return ojtRepository.countByBatchIdAndStatusType(batchId, StatusType.OJT_Fail);
    }

    @Override
    public long countOjtAllStudent(Long batchId) {
        return cvRepository.countByBatchIdAndStatus_StatusType(batchId, StatusType.Offer_Accept);
    }

    //Mg Thant

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public List<OJT> getAllOJT() {
        // TODO Auto-generated method stub
        return ojtRepository.findAll();
    }

    @Override
    public List<OJT> getOJTByBatch(Long batchId) {
        Optional<Batch> optional = batchRepository.findById(batchId);
        Batch batch = null;

        if(optional.isPresent()) {
            batch = optional.get();
        } else {
            throw new RuntimeException("Batch id doesn't exist with this batch id");
        }

        List<OJT> ojts = ojtRepository.findAll();
        List<OJT> ojtByBatch = new ArrayList<>();

        for(OJT ojt : ojts) {
            if(ojt.getCv().getBatch().getId() == batch.getId()) {
                ojtByBatch.add(ojt);
            }
        }

        return ojtByBatch;
    }

    @Override
    public Page<OJT> getAllOJTWithoutAttendance(Pageable pageable) {
        return ojtRepository.findAllWithoutAttendance(pageable);
    }

    @Override
    public OJT getOJTById(Long id) {
        // TODO Auto-generated method stub
        return ojtRepository.findById(id).orElseThrow(() -> new RuntimeException("OJT not found"));
    }

    @Override
    public OJT saveOJT(OJTDTO ojtDto) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public OJT updateOJT(Long id, OJTDTO ojtDto) {
        OJT ojt = getOJTById(id);

        // Update bank account
        ojt.setBankAccount(ojtDto.getBankAccount());

        CV cv = ojt.getCv();
        if (cv != null) {
            cv.setName(ojtDto.getName());
            cv.setPhone(ojtDto.getPhone());
            cvRepository.save(cv);

        } else {
            throw new RuntimeException("CV not found for OJT ID: " + id);
        }

        StatusType status = ojtDto.getStatusName();
        if (status != null) {
            Status existStatus = statusRepository.findByStatusType(status);
            if (existStatus.getStatusType().equals(StatusType.OJT_Pass) || existStatus.getStatusType().equals(StatusType.OJT_Fail) || existStatus.getStatusType().equals(StatusType.OJT_Withdraw)) {
                systemUserService.convertOjtIntoInactive(id);
            }
            ojt.setStatus(existStatus);
        } else {
            throw new RuntimeException("Status not found for OJT ID: " + id);
        }

        return ojtRepository.save(ojt);
    }


    @Override
    public void deleteOJT(Long id) throws Exception {
        // TODO Auto-generated method stub
        OJT ojt = getOJTById(id);

        ojtRepository.delete(ojt);
    }

    @Override
    public List<OJT> getPassedOjtsByIds() {
        // TODO Auto-generated method stub
        return null;
    }

    // Htet Wai Yan Soe
    @Override
    public List<OJT> getOJTByStatus() {
        return ojtRepository.findByStatus_StatusType(StatusType.OJT_Active);
    }

}
