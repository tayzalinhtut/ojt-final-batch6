package com.ojt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="useracc")
@Data
public class UserAcc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String staffId;
    private boolean isFirstTimeLogin= true;
    private String password;

    @Column(name="reset_token", unique = true)
    private String resetToken;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
    private String Phone;
    private String StaffName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    private LocalDateTime lastLogin;
}

