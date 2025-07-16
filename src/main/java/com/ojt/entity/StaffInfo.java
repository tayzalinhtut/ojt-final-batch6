package com.ojt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name="StaffInfo")
@Data
public class StaffInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String email;
    //private String name;
    private String staffId;
    private String division;
    private String deptment;
    private String team;
    private String position;

    @Override
    public String toString() {
        return "StaffInfo{id=" + id + ", name="  + "}"; // Example
    }

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private SystemUsers user;

    @OneToOne(mappedBy = "staffInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Instructor instructor;
}
