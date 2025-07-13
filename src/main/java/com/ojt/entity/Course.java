package com.ojt.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
//Nyi Min Htet Lwin
@Entity
@Data
@Table(name = "course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToMany(mappedBy = "courses")
	private List<Instructor> instructors;

	@ManyToMany
	@JoinTable(name = "course_batch", joinColumns = @JoinColumn(name = "course_id"),
	inverseJoinColumns = @JoinColumn(name = "batch_id"))
	private Set<Batch> batches;


}
