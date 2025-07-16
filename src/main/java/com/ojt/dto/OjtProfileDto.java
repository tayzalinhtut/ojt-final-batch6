package com.ojt.dto;

import java.time.LocalDate;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OjtProfileDto extends SystemUserBaseDto{
	
	 private LocalDate startDate;
	 
	
     private LocalDate endDate;
	 
	 
     private String batchName;
	 
	 @NotBlank(message = "education is required")
	    private String education;
	 
	 @NotBlank(message = "phone is required")
	    private String phone;
	 
	 @NotBlank(message = "address is required")
	    private String address;
	 
	 @NotBlank(message = "skill is required")
	    private String skill;
	 
	
	    private String userIdOJT;
	 
	 
	 @NotBlank(message = "bank Acc is required")
     private String bankAcc;
	 
	 @NotBlank(message = "NRC is required")
     private String NRC;
}
