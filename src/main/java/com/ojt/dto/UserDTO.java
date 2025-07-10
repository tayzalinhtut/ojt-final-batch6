package com.ojt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

	private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Division is required")
    private String division;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Staff ID is required")
    private String staffId;

    @NotBlank(message = "Staff name is required")
    private String staffName;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String resetToken; // Optional: only needed during password reset

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(09|\\+?959)\\d{7,9}$", message = "Invalid Myanmar phone number")
    private String phone;

    @NotNull(message = "Role is required")
    private Long roleId;
	
}
