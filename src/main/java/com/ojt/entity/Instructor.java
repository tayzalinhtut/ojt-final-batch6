package com.ojt.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "staffInfo_id", referencedColumnName = "id")
    private StaffInfo staffInfo;

    @ManyToMany
    @JoinTable(
            name = "instructor_course", // junction table
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Courses> courses;

    @OneToMany(mappedBy = "instructor")
    private List<Evaluation> evaluations;
}

