package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

}
