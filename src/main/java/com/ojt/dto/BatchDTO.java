package com.ojt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BatchDTO {
	
	private Long id;

    @NotBlank(message = "*Batch Name is require")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "*Start Date can't be null")
    private LocalDate startDate;

    @NotNull(message = "*End Date can't be nulls")
    private LocalDate endDate;

    private List<Long> courseIds;
	
}
