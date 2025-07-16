package com.ojt.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.ojt.dto.OjtProfileDto;
import com.ojt.dto.StaffProfileDto;
import com.ojt.entity.*;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SystemUserServiceImp implements SystemUserService {
    @Autowired
    private StaffInfoRepository staffRepo;

    @Autowired
    private StatusRepository statusRepo;

    @Autowired
    private SystemUsersRepository userRepo;

    @Autowired
    private DefaultPasswordService passwordService;

    @Autowired
    private InstructorRepository instructorRepo;

    @Autowired
    private OJTRepository ojtRepo;

    @Autowired
    private ExcelHelper excelHelper;

    @Transactional
    public void createSystemUser(StaffProfileDto form) {
        // 1. Create and save SystemUser
        SystemUsers newUser = new SystemUsers();
        newUser.setName(form.getUserName());
        newUser.setEmail(form.getEmail());
        newUser.setFirstTimeLogin(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUserType("Staff");

        Status activeStatus = statusRepo.findByStatusType(StatusType.Active);
        newUser.setStatus(activeStatus);

        // Set password
        String rawPassword = passwordService.generateStaffPassword(form.getUserIdStaff());
        newUser.setPassword(passwordService.encodePassword(rawPassword));

        // Set role
        List<Role> roleAll = form.getRoleList();

        newUser.setRole(roleAll);


        // Save user first
        userRepo.save(newUser);

        // 2. Create and save StaffInfo
        StaffInfo staff = new StaffInfo();
        //staff.setName(form.getUserName()); don't need set name in staffInfo table
        staff.setStaffId(form.getUserIdStaff());
        // staff.setEmail(form.getEmail()); don't need set email in staffInfo table
        staff.setDivision(form.getDivision());
        staff.setDeptment(form.getDepartment());
        staff.setTeam(form.getTeam());
        staff.setPosition(form.getPosition());
        staff.setUser(newUser);

        staffRepo.save(staff);

        // Set bidirectional relationship
        newUser.setStaffInfo(staff);
        userRepo.save(newUser);

        for (int i = 0; i < roleAll.size(); i++) {
            Role userRole = roleAll.get(i);


            if ("Instructor".equals(userRole.getName())) {

                // 3.Create and save Instructor
                Instructor instructor = new Instructor();
                instructor.setStaffInfo(staff);
                instructorRepo.save(instructor);

                // Set bidirectional relationship
                staff.setInstructor(instructor);
                staffRepo.save(staff);

            }

        }
    }

    @Transactional
    public void updateStaffProfile(Long user_id, StaffProfileDto form) {

        SystemUsers user = userRepo.findById(user_id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getEmail().equals(form.getEmail())) {
            if (userRepo.existsByEmail(form.getEmail())) {
                throw new IllegalArgumentException("Email " + form.getEmail() + " already exists");
            }
            user.setEmail(form.getEmail());
        }

        Status userStaus = statusRepo.getById(form.getStatus_id());

        user.setStatus(userStaus);

        // Set role
        List<Role> roleAll = form.getRoleList();

        user.setRole(roleAll);

        // Save user first
        userRepo.save(user);
        // 2. Create and save StaffInfo
        StaffInfo staff = user.getStaffInfo();
        staff.getUser().setName(form.getUserName());
        staff.setStaffId(form.getUserIdStaff());
        if (!user.getEmail().equals(form.getEmail())) {
            if (userRepo.existsByEmail(form.getEmail())) {
                throw new IllegalArgumentException("Email " + form.getEmail() + " already exists");
            }
            staff.getUser().setEmail(form.getEmail());
        }
        staff.setDivision(form.getDivision());
        staff.setDeptment(form.getDepartment());
        staff.setTeam(form.getTeam());
        staff.setPosition(form.getPosition());
        staff.setUser(user);

        staffRepo.save(staff);

        // Set bidirectional relationship
        user.setStaffInfo(staff);
        userRepo.save(user);
        for (int i = 0; i < roleAll.size(); i++) {
            Role userRole = roleAll.get(i);

            if ("Instructor".equals(userRole.getName())) {

                // 3.Create and save Instructor
                Instructor instructor = staff.getInstructor();
                instructor.setStaffInfo(staff);
                instructorRepo.save(instructor);

                // Set bidirectional relationship
                staff.setInstructor(instructor);
                staffRepo.save(staff);

            }
        }

    }

    @Transactional
    public void updateOjtProfile(Long user_id, OjtProfileDto form) {
        SystemUsers user = userRepo.findById(user_id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getEmail().equals(form.getEmail())) {
            if (userRepo.existsByEmail(form.getEmail())) {
                throw new IllegalArgumentException("Email " + form.getEmail() + " already exists");
            }
            user.setEmail(form.getEmail());
        }

        Status userStaus = statusRepo.getById(form.getStatus_id());

        user.setStatus(userStaus);
        userRepo.save(user);

        OJT ojt = user.getOjt();
        ojt.getCv().setName(form.getUserName());

        if (!user.getEmail().equals(form.getEmail())) {
            if (userRepo.existsByEmail(form.getEmail())) {
                throw new IllegalArgumentException("Email " + form.getEmail() + " already exists");
            }
            ojt.getCv().setEmail(form.getEmail());
        }
        ojt.getCv().setNrc(form.getNRC());
        ojt.getCv().setPhone(form.getPhone());
        ojt.setBankAccount(form.getBankAcc());
        ojt.getCv().setAddress(form.getAddress());
        ojt.getCv().setEducation(form.getEducation());
        ojt.getCv().setSkill(form.getSkill());
        ojt.setUser(user);
        ojtRepo.save(ojt);

        user.setOjt(ojt);
        userRepo.save(user);

    }

    public void saveStaffFromExcel(MultipartFile file) {

        try {
            excelHelper.excelToStaffList(file.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
