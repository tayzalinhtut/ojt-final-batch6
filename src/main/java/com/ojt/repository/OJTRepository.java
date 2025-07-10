package com.ojt.repository;

import com.ojt.entity.OJT;
import com.ojt.enumeration.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OJTRepository extends JpaRepository<OJT, Long> {

    @Query("SELECT COUNT(o) FROM OJT o WHERE o.cv.batch.id = :batchId AND o.status.statusType = :statusType")
    long countByBatchIdAndStatusType(@Param("batchId") Long batchId,
                                     @Param("statusType") StatusType statusType);

    @Query("SELECT o FROM OJT o JOIN FETCH o.cv WHERE o.cv.batch.id = :batchId AND o.status.statusType IN :statusTypes")
    List<OJT> findWithCvByBatchIdAndStatusTypeIn(@Param("batchId") Long batchId,
                                                 @Param("statusTypes") List<StatusType> statusTypes);

}
