package com.ojt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "email_reply")
public class EmailReply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String subject;
	private LocalDate created_at;

	@ManyToOne
	@JoinColumn(name = "sent_by", referencedColumnName = "id")
	private User sentBy;

	@ManyToMany(mappedBy = "emailReplies")
	private List<CV> cvList;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

}
