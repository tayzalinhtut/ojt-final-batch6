package com.ojt.config;

import java.io.IOException;
import java.util.Optional;


import com.ojt.entity.StaffInfo;
import com.ojt.entity.SystemUsers;
import com.ojt.repository.StaffInfoRepository;
import com.ojt.repository.SystemUsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private StaffInfoRepository staffRepo;
    private final SystemUsersRepository userAccRepository;

    public CustomLoginSuccessHandler(SystemUsersRepository userAccRepository, StaffInfoRepository staffRepo) {
        this.userAccRepository = userAccRepository;
        this.staffRepo = staffRepo;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String loginInput = userDetails.getUsername();
        System.out.println("Login input : " + loginInput);

        Optional<SystemUsers> userOptional;

        Optional<StaffInfo> staffOptional;
        SystemUsers user;
        if (loginInput.contains("@")) {
            userOptional = userAccRepository.findByEmail(loginInput);
            System.out.println("This is login tryig user " + userOptional.get());
            user = userOptional.orElseThrow(() -> new RuntimeException("User not found for: " + loginInput));
            System.out.println("This is staff tryig user " + user.getStatus().getStatusType());
        } else {
            staffOptional = staffRepo.findByStaffId(loginInput);
            user = staffOptional.get().getUser();

        }

        if ("Inactive".equals(user.getStatus().getStatusType())) {
            response.sendRedirect("/login?error");
            return;
        }
        if (user.isFirstTimeLogin()) {
            // Clear saved request cache cleanly
            request.getSession(true).removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
            response.sendRedirect("/change-password-first");
            return;
        }

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isInstructor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
        boolean isOperator = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATOR"));
        boolean isStudent = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"));
        System.out.println("isAdmin : " + isAdmin);
        System.out.println("isInstructor : " + isInstructor);
        if (isAdmin) {
            // If admin (regardless of other roles), redirect to admin dashboard
            response.sendRedirect("/admin/dashboard");
        } else if (isOperator) {
            response.sendRedirect("/operator/dashboard");
        } else if (isInstructor) {
            response.sendRedirect("/instructor/instructor-dashboard");
        } else if (isStudent) {
            response.sendRedirect("/student/dashboard");
        } else {
            response.sendRedirect("/login?error");
        }

        /*if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/dashboard");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATOR"))) {
            response.sendRedirect("/operator/dashboard");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))) {
            response.sendRedirect("/instructor/instructor-dashboard");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            response.sendRedirect("/student/dashboard");
        } else {
            response.sendRedirect("/login?error");
        }*/
    }
}
