package com.ojt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "evaluation")
public class Evaluation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String note;
	private int teamwork;
	private int leadership;
	private int assignmentUnderstanding;
	private int techicalSkill;
	private int logicalThinking;
	private int errorHandling;
	private int accuracy;
	private int standardOrFormatting;
	private int assignmentCompetence;

	@ManyToOne
	@JoinColumn(name = "instructor_id")
	private Instructor instructor;

	@ManyToOne
	@JoinColumn(name = "ojt_id")
	private OJT ojt;

}
