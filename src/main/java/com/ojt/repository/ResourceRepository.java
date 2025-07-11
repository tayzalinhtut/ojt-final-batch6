package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Resources;

public interface ResourceRepository extends JpaRepository<Resources, Long> {

}
