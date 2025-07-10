package com.ojt.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "instructor")
public class Instructor {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int staffId;
    private String name;
    private String email;
    private String position;
    private String password;

    @ManyToMany
    @JoinTable(name = "instructor_course", joinColumns = @JoinColumn(name = "instructor_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;

    @OneToMany(mappedBy = "instructor")
    private Set<Evaluation> evaluations;

    //Nyi Min Htet Lwin
    public void addCourse(Course course) {
        this.courses.add(course);
        if (!course.getInstructors().contains(this)) {
            course.getInstructors().add(this);
        }
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        if (course.getInstructors().contains(this)) {
            course.getInstructors().remove(this);
        }
    }

    public void addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        evaluation.setInstructor(this);
    }

    public void removeEvaluation(Evaluation evaluation) {
        this.evaluations.remove(evaluation);
        evaluation.setInstructor(null);
    }

}
