package com.ojt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class InstructorDTO {

	private Long id;

    @NotNull(message = "Staff ID is required")
    private Integer staffId;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Position is required")
    private String position;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private List<Long> courseIds;

}
