package com.ojt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ojt")
public class OJT {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String ojtId;

	private String bankAccount;

	@OneToOne
	@JoinColumn(name = "user_id")
	private SystemUsers user;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@OneToOne
	@JoinColumn(name = "cv_id")
	private CV cv;

	@OneToMany(mappedBy = "ojt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Attendance> attendanceList;

	@ManyToMany
	@JoinTable(name = "ojt_email_reply", joinColumns = @JoinColumn(name = "ojt_id"),
			inverseJoinColumns = @JoinColumn(name = "email_reply_id"))
	private List<EmailReply> emailReplies;

	@OneToMany(mappedBy = "ojt")
	private List<Evaluation> evaluations;

	public OJT(Long id, String bankAccount, Status status, CV cv, Batch batch) {
		super();
		this.id = id;
		this.bankAccount = bankAccount;
		this.status = status;
		this.cv = cv;
	}

}