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

	//Nyi Min Htet Lwin
	public void addInstructor(Instructor instructor) {
		this.instructors.add(instructor);
		if (!instructor.getCourses().contains(this)) {
			instructor.getCourses().add(this);
		}
	}

	public void removeInstructor(Instructor instructor) {
		this.instructors.remove(instructor);
		if (instructor.getCourses().contains(this)) {
			instructor.getCourses().remove(this);
		}
	}

	public void addBatch(Batch batch) {
		this.batches.add(batch);
		if (!batch.getCourses().contains(this)) {
			batch.getCourses().add(this);
		}
	}

	public void removeBatch(Batch batch) {
		this.batches.remove(batch);
		if (batch.getCourses().contains(this)) {
			batch.getCourses().remove(this);
		}
	}

}
