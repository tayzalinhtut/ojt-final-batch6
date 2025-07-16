package com.ojt.dto;
import java.time.LocalDate;

import com.ojt.entity.Role;
import com.ojt.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserProfileDto {
	
		  @Email(message = "Invalid email format")
		    @NotBlank(message = "Email is required")
		    private String email;
            
            private LocalDate startDate;
            private LocalDate endDate;
            private String batchName;
		    private String education;
		    private String phone;
		    private String address;
		    private String skill;
		    private String userIdOJT;
            private Status status;
            private String bankAcc;
            private String NRC;
		    
		    @NotBlank(message = "Staff ID is required")
		    private String userIdStaff;

		    @NotBlank(message = "Staff name is required")
		    private String userName;

		    @NotBlank(message = "Position  is required")
		    private String position;

		    @NotNull(message = "Division is required")
		    private String division;
		    
		    @NotNull(message = "Deptment is required")
		    private String department;
		    
		    @NotNull(message = "Tream is required")
		    private String team;
			
		   
		    private Role role;
             
		    
	        

}
