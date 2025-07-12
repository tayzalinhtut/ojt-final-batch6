package com.ojt.repository;

import com.ojt.entity.OJT;
import com.ojt.enumeration.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OJTRepository extends JpaRepository<OJT, Long> {
    //Tay Za Lin Htut

    @Query("SELECT COUNT(o) FROM OJT o WHERE o.cv.batch.id = :batchId AND o.status.statusType = :statusType")
    long countByBatchIdAndStatusType(@Param("batchId") Long batchId,
                                     @Param("statusType") StatusType statusType);

    @Query("SELECT COUNT(o) FROM OJT o WHERE o.status.statusType = :statusType1 OR o.status.statusType = :statusType2")
    long countByStatusTypeOrStatusType(@Param("statusType1") StatusType statusType1,
                                       @Param("statusType2") StatusType statusType2);


    @Query("SELECT o FROM OJT o JOIN FETCH o.cv WHERE o.cv.batch.id = :batchId AND o.status.statusType IN :statusTypes")
    List<OJT> findWithCvByBatchIdAndStatusTypeIn(@Param("batchId") Long batchId,
                                                 @Param("statusTypes") List<StatusType> statusTypes);

    //Mg Thant
    @Query("SELECT new com.ojt.entity.OJT(o.id, o.bankAccount, o.status, o.cv, o.cv.batch) FROM OJT o")
    Page<OJT> findAllWithoutAttendance(Pageable pageable);

}
