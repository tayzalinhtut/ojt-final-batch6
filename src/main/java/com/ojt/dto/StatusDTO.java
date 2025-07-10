package com.ojt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class   StatusDTO {

	private Long id;

    @NotNull(message = "Status type is required")
    private String statusType; // Use String for enum binding in forms
	
}
