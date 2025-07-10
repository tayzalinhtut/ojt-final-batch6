package com.ojt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "ojt")
public class OJT {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String bankAccount;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@OneToOne
	@JoinColumn(name = "cv_id")
	private CV cv;

	@OneToMany(mappedBy = "ojt", cascade = CascadeType.ALL)
	private List<Attendance> attendanceList;

	@OneToMany(mappedBy = "ojt")
	private List<Evaluation> evaluations;

}
