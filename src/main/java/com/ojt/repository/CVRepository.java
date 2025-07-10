package com.ojt.repository;

import com.ojt.entity.OJT;
import com.ojt.entity.Status;
import com.ojt.enumeration.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.CV;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CVRepository extends JpaRepository<CV, Long> {
    List<CV> findByBatchIsNull();         // Available
    List<CV> findByBatchId(Long id);
    long countByBatchIdAndStatus_StatusType(Long batchId, StatusType statusType);
//    long countByBatchIdAndStatus_StatusTypeAndStatus_StatusType(Long batchId, StatusType statusType, StatusType statusType1);
//    Status Status(Status status);
//    List<CV> findByBatchIdAndStatus_StatusTypeOrStatus_StatusTypeOrStatus_StatusTypeOrStatus_StatusType(Long batchId, StatusType statusType, StatusType statusType1,StatusType statusType2,StatusType statusType3);

//    @Query("SELECT c FROM CV c WHERE c.batch.id = :batchId AND c.status.statusType IN :statusTypes")
//    List<CV> findByBatchIdAndStatusIn(@Param("batchId") Long batchId,
//                                      @Param("statusTypes") List<StatusType> statusTypes);


    //Htet Linn Aung
    List<CV> findByEmailIn(List<String> emails);
    List<CV> findByStatus_StatusType(StatusType statusType);



}
