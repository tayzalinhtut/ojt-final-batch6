package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

}
