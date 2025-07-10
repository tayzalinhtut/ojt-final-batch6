package com.ojt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EvaluationDTO {

	private Long id;

	@NotNull(message = "Instructor is required")
	private Long instructorId;

	@NotNull(message = "OJT is required")
	private Long ojtId;

	private String note;

	@Min(0)
	@Max(5)
	private int teamwork;

	@Min(0)
	@Max(5)
	private int leadership;
	
	@Min(0)
	@Max(5)
	private int assignmentUnderstanding;

	@Min(0)
	@Max(5)
	private int techicalSkill;
	
	@Min(0)
	@Max(5)
	private int logicalThinking;

	@Min(0)
	@Max(5)
	private int errorHandling;
	
	@Min(0)
	@Max(5)
	private int accuracy;

	@Min(0)
	@Max(5)
	private int standardOrFormatting;
	
	@Min(0)
	@Max(5)
	private int assignmentCompetence;

}
