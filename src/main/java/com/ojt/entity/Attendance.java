package com.ojt.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ojt.enumeration.AttendType;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendType attendType;

    @CreationTimestamp
    private LocalTime createdAt;

    private Boolean action = false;

    @ManyToOne
    @JoinColumn(name = "ojt_id")
    private OJT ojt;
}
