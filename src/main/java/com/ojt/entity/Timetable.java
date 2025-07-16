package com.ojt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

import com.ojt.enumeration.DayOfWeek;

@Entity
@Data
@Table(name = "timetable")
public class Timetable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String time;
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	@ManyToOne
	private Courses courses;
}
