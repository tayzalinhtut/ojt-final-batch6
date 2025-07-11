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


    //Htet Linn Aung
    List<CV> findByEmailIn(List<String> emails);
    List<CV> findByStatus_StatusType(StatusType statusType);



}
