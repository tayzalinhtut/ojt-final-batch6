package com.ojt.repository;

import com.ojt.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffInfoRepository extends JpaRepository<StaffInfo, Long> {
    Optional<StaffInfo> findByStaffId(String staffId);

    @Query("SELECT i from StaffInfo i WHERE i.user.name = :staffName")
    StaffInfo findByStaffName(@Param("staffName") String staffName);
}
