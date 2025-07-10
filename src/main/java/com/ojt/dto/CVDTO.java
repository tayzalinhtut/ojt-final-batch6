package com.ojt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CVDTO {

	private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^(09|\\+?959)\\d{7,9}$", message = "Invalid Myanmar phone number")
    private String phone;

    @NotBlank
    private String nrc;

    @NotBlank
    private String address;

    private String skill;
    private String education;
    @NotNull(message = "Batch is required")
    private Long batchId;

    @Min(0)
    @Max(100)
    private int codeTestMark;

    private Long statusId;

    private String file_path;
	
}
