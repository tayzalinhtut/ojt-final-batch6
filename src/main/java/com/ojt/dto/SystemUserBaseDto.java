package com.ojt.dto;

import java.time.LocalDate;
import java.util.List;

import com.ojt.entity.Role;
import com.ojt.enumeration.StatusType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class SystemUserBaseDto {
	@Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
	
	 @NotBlank(message = "user name is required")
	    private String userName;
	 
	
	    private Long user_id;
	 
	
	    private Long status_id;
	 
	
	    private StatusType status_name;
	 
	 @NotNull(message = "role id  is required")
	    private long role_id;
	 
	    private List<Role> roleList;
	    private String role_name;
}
