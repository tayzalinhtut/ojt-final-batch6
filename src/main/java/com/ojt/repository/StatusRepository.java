package com.ojt.repository;

import com.ojt.enumeration.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByStatusType(StatusType statusType);
}
