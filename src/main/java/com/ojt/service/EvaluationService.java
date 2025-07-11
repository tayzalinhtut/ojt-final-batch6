package com.ojt.service;

import java.util.Map;

public interface EvaluationService {
    Map<String, Integer> getSummedSkillsByOjt(Long ojtId);
}
