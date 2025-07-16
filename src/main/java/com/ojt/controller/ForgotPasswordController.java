package com.ojt.controller;

import java.util.Optional;

import com.ojt.entity.SystemUsers;
import com.ojt.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private JavaMailSender mailSender;  // for sending email

    // Show Forgot Password page
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    // Handle Forgot Password form submission
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email, Model model) {
        Optional<SystemUsers> user = forgotPasswordService.findUserByEmail(email);
        if(user.isPresent()) {
            // Generate reset token and reset link
            String token = forgotPasswordService.createPasswordResetToken(user.get());
            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            // Send reset link via email
            sendResetEmail(user.get().getEmail(), resetLink);

            model.addAttribute("message", "We have sent a password reset link to your email.");
        } else {
            model.addAttribute("error", "No account found with that email.");
        }
        return "forgot-password";
    }

    private void sendResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the following link:\n" + resetLink);
        mailSender.send(message);
    }

    // Show Reset Password page
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        if(forgotPasswordService.isValidResetToken(token)) {
            model.addAttribute("token", token);  // pass token to form
            return "reset-Password";
        } else {
            model.addAttribute("error", "Invalid or expired reset token");
            return "login";  // redirect to login page if token invalid
        }
    }

    // Handle Reset Password submission
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model) {
        if(!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "reset-Password";
        }

        Optional<SystemUsers> user = forgotPasswordService.findUserByResetToken(token);
        if(user.isPresent()) {
            forgotPasswordService.updatePassword(user.get(), newPassword);
            model.addAttribute("message", "Your password has been reset successfully. You can now log in.");
            return "login";
        } else {
            model.addAttribute("error", "Invalid token.");
            return "login";
        }
    }

}

