package com.ojt.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SystemUserDto {
	  @Email(message = "Invalid email format")
	    @NotBlank(message = "Email is required")
	    private String email;

        private String staffId;
	  
	    private Long userIdOJT;

	    
	    private String userId;

	    @NotBlank(message = "Staff name is required")
	    private String staffName;

	    @NotBlank(message = "Position  is required")
	    private String position;

	    @NotNull(message = "Division is required")
	    private String division;
	    
	    @NotNull(message = "Deptment is required")
	    private String department;
	    
	    @NotNull(message = "Tream is required")
	    private String team;
		
	    @NotNull(message = "Role is required")
	    private Long roleId;

}
