package com.ojt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String phone;
	private String email;
	private String division;
	private String department;
	private String staffId;
	private String staffName;
	private String position;
	private String password;
	
	@Column(name="reset_token", unique = true)
	private String resetToken;

	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;

}
