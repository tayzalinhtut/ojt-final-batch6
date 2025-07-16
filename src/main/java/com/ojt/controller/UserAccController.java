package com.ojt.controller;

import java.util.List;
import java.util.Optional;

import com.ojt.entity.Role;
import com.ojt.entity.StaffInfo;
import com.ojt.entity.SystemUsers;
import com.ojt.repository.StaffInfoRepository;
import com.ojt.repository.SystemUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserAccController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemUsersRepository userRepo;

    @Autowired
    private StaffInfoRepository staffRepo;

    // Show login page
    @GetMapping({"", "/login"})
    public String showLoginForm() {
        return "login";  // points to login.html in templates
    }

    /*@GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard";
    }
*/
    @GetMapping("/operator/operator-dashboard")
    public String showOperatorDashboard() {
        return "operator/operator-dashboard";
    }
    @GetMapping("/student/student-dashboard")
    public String showStudentDashboard() {
        return "student/student-dashboard";
    }

    @GetMapping("admin/index")
    public String adminIndex() {
        return "admin/index";  // ဒီနေရာမှာ admin/index.html template ရှိဖို့လိုပါတယ်
    }

    /*@GetMapping("/instructor/instructor-dashboard")
    public String showInstructorDashboard() {
        return "instructor/instructor-dashboard";
    }*/
    // Show first time password change form
    @GetMapping("/change-password-first")
    public String changePasswordForm() {
        return "change-first-time-login-password";  // points to change-first-time-login-password.html
    }

    // Process password change form submission
    @PostMapping("/change-password-first")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {

        // Basic validation
        if (newPassword == null || newPassword.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            return "change-first-time-login-password";
        }

        String loginInput = userDetails.getUsername();
        Optional<SystemUsers>  userOptional ;
        Optional<StaffInfo> staffOptional;

        SystemUsers user;
        if (loginInput.contains("@")) {
            userOptional = userRepo.findByEmail(loginInput);
            System.out.println("This is login tryig user "+ userOptional.get());
            user = userOptional.orElseThrow(() -> new RuntimeException("User not found for: " + loginInput));

        } else {
            staffOptional=staffRepo.findByStaffId(loginInput);
            user=staffOptional.get().getUser();

        }




        // Update password and set firstTimeLogin false
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstTimeLogin(false);
        userRepo.save(user);

        // Redirect user based ®n role
        List<String> priority = List.of("ADMIN", "OPERATOR", "INSTRUCTOR", "STUDENT");
        String roleName = user.getRole().stream()
                .map(role -> role.getName().toUpperCase())
                .sorted((r1, r2) -> Integer.compare(priority.indexOf(r1), priority.indexOf(r2)))
                .findFirst()
                .orElse("LOGIN");

        switch (roleName) {
            case "ADMIN":
                return "redirect:/admin/dashboard";
            case "OPERATOR":
                return "redirect:/operator/operator-dashboard";
            case "INSTRUCTOR":
                return "redirect:/instructor/instructor-dashboard";
            case "STUDENT":
                return "redirect:/student/student-dashboard";
            default:
                return "redirect:/login";
        }
        /*String roleName="ADMIN";
        List<Role> userRole=user.getRole();

        for (int i = 0; i < userRole.size(); i++) {
            Role role = userRole.get(i);

            if (!"ADMIN".equals(role.getName())) {
                roleName = role.getName().toUpperCase();

            }
        }
        switch (roleName) {
            case "ADMIN":
                return "redirect:/admin/dashboard";
            case "OPERATOR":
                return "redirect:/operator/operator-dashboard";
            case "INSTRUCTOR":
                return "redirect:/instructor/instructor-dashboard";
            case "STUDENT":
                return "redirect:/student/student-dashboard";
            default:
                return "redirect:/login";
        }*/
    }

}
