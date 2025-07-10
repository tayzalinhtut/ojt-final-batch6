package com.ojt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceDTO {

	private Long id;

    @NotNull
    private Long ojtId;

    @NotNull
    private LocalDate date;

    @NotNull(message = "Attend type is required")
    private String attendType;

    private Boolean action = Boolean.FALSE;
	
}
