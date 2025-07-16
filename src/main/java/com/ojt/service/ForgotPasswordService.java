package com.ojt.service;

import java.util.Optional;
import java.util.UUID;

import com.ojt.entity.SystemUsers;
import com.ojt.repository.SystemUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Service
public class ForgotPasswordService {
private final UserAccService userAccService;
	
	@Autowired
	private SystemUsersRepository userAccRepository;

    ForgotPasswordService(UserAccService userAccService) {
        this.userAccService = userAccService;
    }
	
	// Create a password reset token and store it in the database
	@Transactional
	public String createPasswordResetToken(SystemUsers user) {
		String token=UUID.randomUUID().toString(); //Generate a unique token
		user.setResetToken(token); //Set the reset token
		userAccRepository.save(user);// Save the user with the token
		return token;
	}
	
	//Find a user by email
	public Optional<SystemUsers> findUserByEmail(String email){
		return userAccRepository.findByEmail(email);
	}
	
	//Validate the reset token
	
	public boolean isValidResetToken(String token) {
		return userAccRepository.existsByResetToken(token); //Check if the token exists in the databases
	}
	
	// Find a user by reset token
	
	public Optional<SystemUsers> findUserByResetToken(String token){
		return userAccRepository.findByResetToken(token);
	}
	
	// Update the password
	
	public void updatePassword(SystemUsers user , String newPassword) {
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword)); // Encrypt the new password
        user.setResetToken(null); // Clear the reset token after password reset
        userAccRepository.save(user); // Save the updated user
    }

}
