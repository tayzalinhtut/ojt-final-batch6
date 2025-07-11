package com.ojt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "cv")
public class CV {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String nrc;
    private String address;
    private String skill;
    private String education;
    private int codeTestMark;
    private String file_path;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resources resource;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "cv_email_reply", joinColumns = @JoinColumn(name = "cv_id"),
            inverseJoinColumns = @JoinColumn(name = "email_reply_id"))
    @JsonIgnore
    private List<EmailReply> emailReplies;

    @OneToOne(mappedBy = "cv")
    private OJT ojt;

}
