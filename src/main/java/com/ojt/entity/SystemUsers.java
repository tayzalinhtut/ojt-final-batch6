package com.ojt.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.ojt.enumeration.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SystemUsers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private boolean isFirstTimeLogin = true;
    private String password;
    private LocalDateTime createdAt;
    private String userType;
    @Column(name = "reset_token", unique = true)
    private String resetToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> role;

    @ManyToOne
    @JoinColumn(name = "status_id",referencedColumnName = "id")
    private Status status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private StaffInfo staffInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private OJT ojt;

    @Override
    public String toString() {
        return "SystemUsers{id=" + user_id + ", username=" + name + "}";
    }
}
