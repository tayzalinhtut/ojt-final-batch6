package com.ojt.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
	private int technicalSkill;
	private int logicalThinking;
	private int errorHandling;
	private int accuracy;
	private int standardOrFormatting;
	private int assignmentCompetence;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdDate;

	@ManyToOne
	@JoinColumn(name = "instructor_id")
	private Instructor instructor;

	@ManyToOne
	@JoinColumn(name = "ojt_id")
	private OJT ojt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id")
	private Course course;

	public int getTotalScore() {
		return teamwork + leadership + assignmentUnderstanding + technicalSkill +
				logicalThinking + errorHandling + accuracy + standardOrFormatting +
				assignmentCompetence;
	}
}
