package com.ojt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.File;

@ControllerAdvice
public class BaseController {

    @ModelAttribute("isAdminAndInstructor")
    public boolean isAdminAndInstructor(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities() != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
            boolean isInstructor = authentication.getAuthorities().stream()
                    .anyMatch(g -> g.getAuthority().equals("ROLE_INSTRUCTOR"));
            return isAdmin && isInstructor;
        }
        return false;
    }
}

