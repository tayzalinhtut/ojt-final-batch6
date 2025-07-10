package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
