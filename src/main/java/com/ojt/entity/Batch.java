package com.ojt.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "batch")
public class Batch {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany(mappedBy = "batches")
    private List<Course> courses;

    @OneToMany(mappedBy = "batch")
    private List<CV> cvs;
}