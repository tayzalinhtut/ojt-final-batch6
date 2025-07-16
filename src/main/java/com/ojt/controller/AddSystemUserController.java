package com.ojt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ojt.dto.OjtProfileDto;
import com.ojt.dto.StaffProfileDto;
import com.ojt.entity.Role;
import com.ojt.entity.Status;
import com.ojt.entity.SystemUsers;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.RoleRepository;
import com.ojt.repository.StatusRepository;
import com.ojt.repository.SystemUsersRepository;
import com.ojt.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/admin")

public class AddSystemUserController {




    private final SystemUserService systemUserService;
    private final RoleRepository roleRepository;
    private final SystemUsersRepository systemUserRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public AddSystemUserController(SystemUserService systemUserService,
                                   RoleRepository roleRepository,
                                   SystemUsersRepository systemUserRepository,
                                   StatusRepository statusRepository) {
        this.systemUserService = systemUserService;
        this.roleRepository = roleRepository;
        this.systemUserRepository=systemUserRepository;
        this.statusRepository=statusRepository;
    }

    @GetMapping("/user-management")
    public String showStaffMembersAdd(Model model) {

        List<SystemUsers> userList=systemUserRepository.findAll();
        model.addAttribute("userList", userList);

        return "admin/users/user-management";
    }



    @GetMapping("/add-staff-member")
    public String showStaffAddMember(Model model) {
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("userDto", new StaffProfileDto());
        return "admin/users/user-add";
    }


    @GetMapping("/view-user-profile/{user_id}")
    public String showUserProfile(@PathVariable Long user_id,Model model) {

        Optional<SystemUsers> userOptional = systemUserRepository.findById(user_id);
        if(userOptional.isEmpty()) {
            // Handle case when user not found
            return "redirect:/admin/user-management?error=User not found";
        }

        SystemUsers profile = userOptional.get();


        if("Staff".equals(profile.getUserType())) {
            StaffProfileDto profileDto=new StaffProfileDto();
            // Map entity to DTO
            profileDto.setStatus_name(profile.getStatus().getStatusType());
            profileDto.setUserIdStaff(profile.getStaffInfo().getStaffId()) ;
            profileDto.setUserName(profile.getName()) ;
            profileDto.setEmail(profile.getEmail());
            profileDto.setDivision(profile.getStaffInfo().getDivision());
            profileDto.setDepartment(profile.getStaffInfo().getDeptment());
            profileDto.setPosition(profile.getStaffInfo().getPosition());
            System.out.print("this is position"+profile.getStaffInfo().getPosition());
            profileDto.setTeam(profile.getStaffInfo().getTeam());

            System.out.print("This is user team"+profile.getStaffInfo().getTeam());
            List<Role> rolesArr = new ArrayList<>();
            rolesArr=profile.getRole();
            profileDto.setRoleList(rolesArr);
            // profileDto.setRole_name(profile.getRole().getName());
            // Set other fields as needed
            model.addAttribute("userProfile", profileDto);
            return "/admin/users/user-view";
        }
        else {
            OjtProfileDto profileDto=new OjtProfileDto();

            profileDto.setStatus_name(profile.getStatus().getStatusType());
            profileDto.setUserIdOJT(profile.getOjt().getOjtId());
            profileDto.setUserName(profile.getName());
            profileDto.setEmail(profile.getEmail());
            profileDto.setPhone(profile.getOjt().getCv().getPhone());
            profileDto.setAddress(profile.getOjt().getCv().getAddress());
            profileDto.setEducation(profile.getOjt().getCv().getEducation());
            profileDto.setSkill(profile.getOjt().getCv().getSkill());
            List<Role> rolesArr = new ArrayList<>();
            rolesArr=profile.getRole();
            profileDto.setRoleList(rolesArr);
            //profileDto.setRole_name(profile.getRole().getName());
            profileDto.setStartDate(profile.getOjt().getCv().getBatch().getStartDate());
            profileDto.setEndDate(profile.getOjt().getCv().getBatch().getEndDate());
            profileDto.setBatchName(profile.getOjt().getCv().getBatch().getName());
            model.addAttribute("userProfile", profileDto);
            return "admin/member-profile-view";

        }



    }
    @GetMapping("/view-member-profile")
    public String showOJTProfile()
    {
        return "admin/member-profile-view";
    }

    @GetMapping("/view-staff-member")
    public String showStaffProfile(){
        return "admin/users/user-view";
    }


    @GetMapping("/edit-staff-member/{user_id}")
    public String showEditUserProfile(@PathVariable Long user_id,Model model) {
        List<Role> roleAll=roleRepository.findAll();
        model.addAttribute("user_id", user_id);
        model.addAttribute("allRoles", roleAll);
        List<Status> allStatuses = statusRepository.findAll();


        // 2. Filter only Active and Inactive statuses
        List<Status> filteredStatuses = allStatuses.stream()
                .filter(status -> StatusType.Active.equals(status.getStatusType()) || StatusType.Inactive.equals(status.getStatusType()))
                .collect(Collectors.toList());

        // 3. Add to model
        model.addAttribute("statusList", filteredStatuses);
        Optional<SystemUsers> userOptional=systemUserRepository.findById(user_id);
        if(userOptional.isEmpty()) {
            // Handle case when user not found
            return "redirect:/admin/user-management?error=User not found";
        }
        SystemUsers profile = userOptional.get();


        if("Staff".equals(profile.getUserType())){

            StaffProfileDto profileDto=new StaffProfileDto();
            profileDto.setUserName(profile.getName());
            profileDto.setEmail(profile.getEmail());
            profileDto.setStatus_name(profile.getStatus().getStatusType());
            System.out.println("THis is user status " + profile.getStatus().getStatusType());
            profileDto.setUserIdStaff(profile.getStaffInfo().getStaffId());
            profileDto.setDivision(profile.getStaffInfo().getDivision());
            List<Role> rolesArr = new ArrayList<>();
            rolesArr=profile.getRole();
            profileDto.setRoleList(rolesArr);
            //profileDto.setRole_name(profile.getRole().getName());
            profileDto.setDepartment(profile.getStaffInfo().getDeptment());
            profileDto.setTeam(profile.getStaffInfo().getTeam());
            profileDto.setPosition(profile.getStaffInfo().getPosition());
            model.addAttribute("userProfile", profileDto);

            return "admin/users/user-edit";
        }
        else {

            OjtProfileDto profileDto=new OjtProfileDto();
            profileDto.setUserName(profile.getName());
            profileDto.setEmail(profile.getEmail());
            profileDto.setAddress(profile.getOjt().getCv().getAddress());
            List<Role> rolesArr = new ArrayList<>();
            rolesArr=profile.getRole();
            profileDto.setRoleList(rolesArr);
            // profileDto.setRole_name(profile.getRole().getName());
            profileDto.setNRC(profile.getOjt().getCv().getNrc());
            profileDto.setPhone(profile.getOjt().getCv().getPhone());
            profileDto.setBankAcc(profile.getOjt().getBankAccount());
            profileDto.setEducation(profile.getOjt().getCv().getEducation());
            profileDto.setSkill(profile.getOjt().getCv().getSkill());
            model.addAttribute("userProfile", profileDto);
            return "admin/member-profile-edit";
        }
    }


    @GetMapping("/staff-member-edit")
    public String showStaffMemberEditPage() {
        return "admin/user-management/user-edit";
    }
    @GetMapping("/member-profile-edit")
    public String showMemberProfileEditPage() {
        return "admin/member-profile-edit";
    }
    @PostMapping("/delete-staff-member/{user_id}")
    public String deleteSystemUser(@PathVariable Long user_id,@Valid @ModelAttribute("userProfile") StaffProfileDto userProfile,
                                   BindingResult result,
                                   Model model) {
        Optional<SystemUsers> user=systemUserRepository.findById(user_id);
        if(user.isEmpty()) {
            // Handle case when user not found
            return "redirect:/admin/user-management?error=User not found";
        }
        SystemUsers profile = user.get();
        systemUserRepository.deleteById(user_id);
        System.out.println("Delete is successfully");
        return "redirect:/admin/user-management?success";
    }
    @PostMapping("/update_staff/{user_id}")
    public String updateStaffMember(@PathVariable Long user_id,@Valid @ModelAttribute("userProfile") StaffProfileDto userProfile,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/users/user-edit";  // Fixed: Return to add form
        }
        try {
            System.out.println("Service is calling: " + user_id+userProfile);
            Optional<SystemUsers> userOptional=systemUserRepository.findById(user_id);
            if(userOptional.isEmpty()) {
                // Handle case when user not found
                return "redirect:/admin/user-management?error=User not found";
            }
            SystemUsers profile = userOptional.get();

            systemUserService.updateStaffProfile(user_id,userProfile);
            System.out.println("Service is called successfully");
            return "redirect:/admin/user-management?success";

        } catch (Exception e) {
            System.out.println("Controller update staff is error : " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "User creating is wrong: " + e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/users/user-edit";
        }

    }

    @PostMapping("/update_ojt/{user_id}")
    public String updateStaffMember(@PathVariable Long user_id,@Valid @ModelAttribute("userProfile") OjtProfileDto userProfile,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/users/user-edit";  // Fixed: Return to add form
        }
        try {
            System.out.println("Service calling: " + user_id+userProfile);
            Optional<SystemUsers> userOptional=systemUserRepository.findById(user_id);
            if(userOptional.isEmpty()) {
                // Handle case when user not found
                return "redirect:/admin/user-management?error=User not found";
            }


            systemUserService.updateOjtProfile(user_id,userProfile);
            System.out.println("Service is successfully");
            return "redirect:/admin/user-management?success";

        } catch (Exception e) {
            System.out.println("Controller update staff is error : " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "User creating is wrong: " + e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/users/user-edit";
        }

    }

    @PostMapping("/save-user")
    public String addStaffMember(
            @Valid @ModelAttribute("userDto") StaffProfileDto userDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                System.out.println("This is save user error "+error.toString()); // Print all errors
            });
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/users/user-add";  // Fixed: Return to add form
        }

        try {
            System.out.println("Service userDto: " + userDto);
            systemUserService.createSystemUser(userDto);
            System.out.println("Service is successfully");
            return "redirect:/admin/user-management?success";
        } catch (Exception e) {
            System.out.println("Controller is error  " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "User creating is wrong " + e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/users/user-add";
        }
    }


    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        systemUserService.saveStaffFromExcel(file);

        // Add a success message
        redirectAttributes.addFlashAttribute("message", "Successfully registration!");
        return "redirect:/admin/user-management?success";
    }

}
