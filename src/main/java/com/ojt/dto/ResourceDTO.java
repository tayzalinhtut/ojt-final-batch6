package com.ojt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResourceDTO {
    private Long id;

    @NotBlank(message = "Resource name is required")
    private String name;
}