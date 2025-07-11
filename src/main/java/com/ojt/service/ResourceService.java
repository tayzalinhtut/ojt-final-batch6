package com.ojt.service;

import com.ojt.dto.ResourceDTO;
import com.ojt.entity.Resources;

import java.util.List;

public interface ResourceService {
    Resources addResource(ResourceDTO resourceDTO);

    List<Resources> getAllResource();

    Resources getResourceById(Long id);

    Resources updateResource(Long id, ResourceDTO resourceDTO);

    void deleteResource(Long id);
}
