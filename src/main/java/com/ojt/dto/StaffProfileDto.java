package com.ojt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffProfileDto extends SystemUserBaseDto {
	    @NotBlank(message = "Staff ID is required")
	    private String userIdStaff;

	    @NotBlank(message = "Position  is required")
	    private String position;

	    @NotNull(message = "Division is required")
	    private String division;
	    
	    @NotNull(message = "Deptment is required")
	    private String department;
	    
	    @NotNull(message = "Tream is required")
	    private String team;

}
