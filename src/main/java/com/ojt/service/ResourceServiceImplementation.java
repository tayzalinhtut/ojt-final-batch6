package com.ojt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ojt.dto.ResourceDTO;
import com.ojt.entity.Resources;
import com.ojt.repository.ResourceRepository;

@Service
public class ResourceServiceImplementation implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Resources addResource(ResourceDTO resourceDTO) {
        Resources resource = new Resources();
        resource.setName(resourceDTO.getName());
        return resourceRepository.save(resource);
    }

    @Override
    public List<Resources> getAllResource() {
        return resourceRepository.findAll();
    }

    @Override
    public Resources getResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }

    @Override
    public Resources updateResource(Long id, ResourceDTO resourceDTO) {
        Resources resource = getResourceById(id);
        resource.setName(resourceDTO.getName());
        return resourceRepository.save(resource);
    }

    @Override
    public void deleteResource(Long id) {
        Resources resource = getResourceById(id);
        resourceRepository.delete(resource);
    }
}