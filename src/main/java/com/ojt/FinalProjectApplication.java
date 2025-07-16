package com.ojt;

import com.ojt.entity.*;
import com.ojt.enumeration.AttendType;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.*;
import com.ojt.service.DefaultPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories("com.ojt.repository")  // Explicit repository package
@EntityScan("com.ojt.entity")
@EnableAsync
public class FinalProjectApplication {

    @Autowired
    private DefaultPasswordService passwordService;

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StaffInfoRepository staffRepo, SystemUsersRepository userRepo, RoleRepository roleRepository, CVRepository cvRepo, OJTRepository ojtRepo, CourseRepository courseRepo, StatusRepository statusRepo, BatchRepository batchRepo, InstructorRepository instructorRepo, AttendanceRepository attendanceRepo, EvaluationRepository evaluationRepository) {
        return args -> {

            Role role = new Role();
            role.setName("ADMIN");
            roleRepository.save(role);

            Role instructor = new Role();
            instructor.setName("INSTRUCTOR");
            roleRepository.save(instructor);

            Role operator = new Role();
            operator.setName("OPERATOR");
            roleRepository.save(operator);

            Role ojtRole = new Role();
            ojtRole.setName("STUDENT");
            roleRepository.save(ojtRole);

            Batch batch1 = new Batch();
            batch1.setName("Batch1");
            batch1.setStartDate(LocalDate.of(2020, 1, 1));
            batch1.setEndDate(LocalDate.of(2020, 12, 1));
            batch1 = batchRepo.save(batch1);  // save and reuse with ID
            // Create and save status
            Status interFail = new Status();
            interFail.setStatusType(StatusType.Interview_Fail);
            interFail = statusRepo.save(interFail);

            Status scanPass = new Status();
            scanPass.setStatusType(StatusType.Scan_Pass);
            scanPass = statusRepo.save(scanPass);

            Status interPass = new Status();
            interPass.setStatusType(StatusType.Interview_Pass);
            interPass = statusRepo.save(interPass);

            Status ojtW = new Status();
            ojtW.setStatusType(StatusType.OJT_Withdraw);
            ojtW = statusRepo.save(ojtW);

            Status ojtActive = new Status();
            ojtActive.setStatusType(StatusType.OJT_Active);
            ojtActive = statusRepo.save(ojtActive);

            Status ojtP = new Status();
            ojtP.setStatusType(StatusType.OJT_Pass);
            ojtP = statusRepo.save(ojtP);

            Status ojtF = new Status();
            ojtF.setStatusType(StatusType.OJT_Fail);
            ojtF = statusRepo.save(ojtF);

            Status codeP = new Status();
            codeP.setStatusType(StatusType.CodeTest_Pass);
            codeP = statusRepo.save(codeP);

            Status codeF = new Status();
            codeF.setStatusType(StatusType.CodeTest_Fail);
            codeF = statusRepo.save(codeF);

            Status offerAccepted = new Status();
            offerAccepted.setStatusType(StatusType.Offer_Accept);
            offerAccepted = statusRepo.save(offerAccepted);

            Status scanPassed = new Status();
            scanPassed.setStatusType(StatusType.Scan_Pass);
            scanPassed = statusRepo.save(scanPassed);

            Status scanFail = new Status();
            scanFail.setStatusType(StatusType.Scan_Fail);
            scanFail = statusRepo.save(scanFail);

            Status emailInvited = new Status();
            emailInvited.setStatusType(StatusType.Email_InterviewInvite);
            emailInvited = statusRepo.save(emailInvited);

            Status emailCodeInvite = new Status();
            emailCodeInvite.setStatusType(StatusType.Email_CodeTestInvite);
            emailCodeInvite = statusRepo.save(emailCodeInvite);

            Status emailInterviewResult = new Status();
            emailInterviewResult.setStatusType(StatusType.Email_InterviewResult);
            emailInterviewResult = statusRepo.save(emailInterviewResult);

            Status emailCodetestFail = new Status();
            emailCodetestFail.setStatusType(StatusType.Email_CodeTestFail);
            emailCodetestFail = statusRepo.save(emailCodetestFail);

            Status emailInterFail = new Status();
            emailInterFail.setStatusType(StatusType.Email_InterviewFail);
            emailInterFail = statusRepo.save(emailInterFail);

            Status accActive = new Status();
            accActive.setStatusType(StatusType.Active);
            accActive = statusRepo.save(accActive);

            Status accInactive = new Status();
            accInactive.setStatusType(StatusType.Inactive);
            accInactive = statusRepo.save(accInactive);

            // 20 CVs
            CV cv1 = new CV();
            cv1.setName("CV1");
            cv1.setEmail("cv1@mail.com");
            cv1.setPhone("111");
            cv1.setStatus(offerAccepted);
            CV cv2 = new CV();
            cv2.setName("CV2");
            cv2.setEmail("cv2@mail.com");
            cv2.setPhone("112");
            cv2.setStatus(offerAccepted);
            CV cv3 = new CV();
            cv3.setName("CV3");
            cv3.setEmail("cv3@mail.com");
            cv3.setPhone("113");
            cv3.setStatus(offerAccepted);
            CV cv4 = new CV();
            cv4.setName("CV4");
            cv4.setEmail("cv4@mail.com");
            cv4.setPhone("114");
            cv4.setStatus(offerAccepted);
            CV cv5 = new CV();
            cv5.setName("CV5");
            cv5.setEmail("cv5@mail.com");
            cv5.setPhone("115");
            cv5.setStatus(offerAccepted);
            CV cv6 = new CV();
            cv6.setName("CV6");
            cv6.setEmail("cv6@mail.com");
            cv6.setPhone("116");
            cv6.setStatus(offerAccepted);
            CV cv7 = new CV();
            cv7.setName("CV7");
            cv7.setEmail("cv7@mail.com");
            cv7.setPhone("117");
            cv7.setStatus(offerAccepted);
            CV cv8 = new CV();
            cv8.setName("CV8");
            cv8.setEmail("cv8@mail.com");
            cv8.setPhone("118");
            cv8.setStatus(offerAccepted);
            CV cv9 = new CV();
            cv9.setName("CV9");
            cv9.setEmail("cv9@mail.com");
            cv9.setPhone("119");
            cv9.setStatus(offerAccepted);
            CV cv10 = new CV();
            cv10.setName("CV10");
            cv10.setEmail("cv10@mail.com");
            cv10.setPhone("120");
            cv10.setStatus(offerAccepted);
            CV cv11 = new CV();
            cv11.setName("CV11");
            cv11.setEmail("tayzalinhtut@gmail.com");
            cv11.setPhone("121");
            cv11.setStatus(interPass);
            CV cv12 = new CV();
            cv12.setName("CV12");
            cv12.setEmail("cv12@mail.com");
            cv12.setPhone("122");
            cv12.setStatus(interFail);
            CV cv13 = new CV();
            cv13.setName("CV13");
            cv13.setEmail("cv13@mail.com");
            cv13.setPhone("123");
            cv13.setStatus(interFail);
            CV cv14 = new CV();
            cv14.setName("CV14");
            cv14.setEmail("cv14@mail.com");
            cv14.setPhone("124");
            cv14.setStatus(interFail);
            CV cv15 = new CV();
            cv15.setName("CV15");
            cv15.setEmail("cv15@mail.com");
            cv15.setPhone("125");
            cv15.setStatus(interFail);
            CV cv16 = new CV();
            cv16.setName("CV16");
            cv16.setEmail("cv16@mail.com");
            cv16.setPhone("126");
            cv16.setStatus(interFail);
            CV cv17 = new CV();
            cv17.setName("CV17");
            cv17.setEmail("cv17@mail.com");
            cv17.setPhone("127");
            cv17.setStatus(interFail);
            CV cv18 = new CV();
            cv18.setName("CV18");
            cv18.setEmail("cv18@mail.com");
            cv18.setPhone("128");
            cv18.setStatus(interFail);
            CV cv19 = new CV();
            cv19.setName("CV19");
            cv19.setEmail("cv19@mail.com");
            cv19.setPhone("129");
            cv19.setStatus(interFail);
            CV cv20 = new CV();
            cv20.setName("CV20");
            cv20.setEmail("cv20@mail.com");
            cv20.setPhone("130");
            cv20.setStatus(interFail);
            cv1.setBatch(batch1);
            cv2.setBatch(batch1);
            cv3.setBatch(batch1);
            cv4.setBatch(batch1);
            cv5.setBatch(batch1);
            cv6.setBatch(batch1);
            cv7.setBatch(batch1);
            cv8.setBatch(batch1);
            cv9.setBatch(batch1);
            cv10.setBatch(batch1);
            cv11.setBatch(batch1);
            cv12.setBatch(batch1);
            cv13.setBatch(batch1);
            cv14.setBatch(batch1);
            cv15.setBatch(batch1);
            cv16.setBatch(batch1);
            cv17.setBatch(batch1);
            cv18.setBatch(batch1);
            cv19.setBatch(batch1);
            cv20.setBatch(batch1);


            cvRepo.saveAll(Arrays.asList(cv1, cv2, cv3, cv4, cv5, cv6, cv7, cv8, cv9, cv10,
                    cv11, cv12, cv13, cv14, cv15, cv16, cv17, cv18, cv19, cv20));


            Courses course1 = new Courses();
            course1.setName("Java");


            Courses course2 = new Courses();
            course2.setName("Spring Boot");

            Courses course3 = new Courses();
            course3.setName("ReactJS");

            Courses course4 = new Courses();
            course4.setName("Python");

            Courses course5 = new Courses();
            course5.setName("Docker");
            courseRepo.saveAll(Arrays.asList(course1, course2, course3, course4, course5));

            OJT ojt1 = new OJT();
            ojt1.setBankAccount("Acc1");
            ojt1.setStatus(ojtActive);
            ojt1.setCv(cv1);
            OJT ojt2 = new OJT();
            ojt2.setBankAccount("Acc2");
            ojt2.setStatus(ojtActive);
            ojt2.setCv(cv2);
//            OJT ojt3 = new OJT();
//            ojt3.setBankAccount("Acc3");
//            ojt3.setStatus(ojtActive);
//            ojt3.setCv(cv3);
//            OJT ojt4 = new OJT();
//            ojt4.setBankAccount("Acc4");
//            ojt4.setStatus(ojtActive);
//            ojt4.setCv(cv4);
//            OJT ojt5 = new OJT();
//            ojt5.setBankAccount("Acc5");
//            ojt5.setStatus(ojtActive);
//            ojt5.setCv(cv5);
//            OJT ojt6 = new OJT();
//            ojt6.setBankAccount("Acc6");
//            ojt6.setStatus(ojtActive);
//            ojt6.setCv(cv6);
//            OJT ojt7 = new OJT();
//            ojt7.setBankAccount("Acc7");
//            ojt7.setStatus(ojtActive);
//            ojt7.setCv(cv7);
//            OJT ojt8 = new OJT();
//            ojt8.setBankAccount("Acc8");
//            ojt8.setStatus(ojtP);
//            ojt8.setCv(cv8);
//            OJT ojt9 = new OJT();
//            ojt9.setBankAccount("Acc9");
//            ojt9.setStatus(ojtW);
//            ojt9.setCv(cv9);
//            OJT ojt10 = new OJT();
//            ojt10.setBankAccount("Acc10");
//            ojt10.setStatus(ojtF);
//            ojt10.setCv(cv10);

//            ojtRepo.saveAll(Arrays.asList(ojt1, ojt2, ojt3, ojt4, ojt5, ojt6, ojt7, ojt8, ojt9, ojt10));
            ojtRepo.saveAll(Arrays.asList(ojt1, ojt2));

            LocalTime currentTime = LocalTime.now();
            Attendance attendance1 = new Attendance();
            attendance1.setDate(LocalDate.of(2025, 7, 1));
            attendance1.setAttendType(AttendType.Attend);
            attendance1.setOjt(ojt1);
            attendance1.setAction(false);
            attendance1.setCreatedAt(currentTime);

            Attendance attendance2 = new Attendance();
            attendance2.setDate(LocalDate.of(2025, 7, 2));
            attendance2.setAttendType(AttendType.Leave);
            attendance2.setOjt(ojt2);
            attendance2.setAction(false);
            attendance2.setCreatedAt(currentTime);

            Attendance attendance3 = new Attendance();
            attendance3.setDate(LocalDate.of(2025, 7, 3));
            attendance3.setAttendType(AttendType.HalfLeave);
            attendance3.setOjt(ojt1);
            attendance3.setAction(false);
            attendance3.setCreatedAt(currentTime);

            attendanceRepo.saveAll(Arrays.asList(attendance1, attendance2, attendance3));
            if (userRepo.findByEmail("ayemohmohkyaw777@gmail.com").isEmpty()) {
                Role role1 = roleRepository.findByName("ADMIN");

                SystemUsers admin = new SystemUsers();
                admin.setEmail("ayemohmohkyaw777@gmail.com");


                admin.setName("Aye Moh Moh Kyaw");
                admin.setFirstTimeLogin(true);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUserType("Staff");

                Status activeStatus = statusRepo.findByStatusType(StatusType.Active);
                System.out.println("Active Status: " + activeStatus);
                admin.setStatus(activeStatus);

                // Set password
                String rawPassword = passwordService.generateStaffPassword("12345");
                admin.setPassword(passwordService.encodePassword(rawPassword));
                // Set role
                List<Role> rolesArr = new ArrayList<>();

                System.out.println("Role: " + role);
                rolesArr.add(instructor);
                rolesArr.add(role);
                admin.setRole(rolesArr);

                // Save user first
                userRepo.save(admin);

                // 2. Create and save StaffInfo
                StaffInfo staff = new StaffInfo();
                staff.setUser(admin);
                staff.getUser().setName("Aye Moh Moh Kyaw");
                staff.setStaffId("66-00000");
                staff.getUser().setEmail("ayemohmohkyaw777@gmail.com");
                // on System User table
                staff.setDivision("Management Division");
                staff.setDeptment("HR/Admin Team");
                staff.setTeam("HR/Admin Team");
                staff.setPosition("Administrator");

                staffRepo.save(staff);

                // Set bidirectional relationship
                admin.setStaffInfo(staff);
                userRepo.save(admin);

                System.out.println("✅ Admin user created successfully.");
                System.out.println("Role test: " + rolesArr);

                SystemUsers ojt11= new SystemUsers();
                ojt11.setEmail(ojt1.getCv().getEmail());
                ojt11.setFirstTimeLogin(true);
                ojt11.setCreatedAt(LocalDateTime.now());
                ojt11.setUserType("OJT");
                List<Role> rolesArr2=new ArrayList<>();
                rolesArr2.add(ojtRole);
                ojt11.setRole(rolesArr2);
                ojt11.setName(ojt1.getCv().getName());
                ojt11.setStatus(accActive);
                userRepo.save(ojt11);

                SystemUsers ojt12= new SystemUsers();
                ojt12.setEmail(ojt2.getCv().getEmail());
                ojt12.setFirstTimeLogin(true);
                ojt12.setCreatedAt(LocalDateTime.now());
                ojt12.setUserType("OJT");
                List<Role> rolesArr3=new ArrayList<>();
                rolesArr3.add(ojtRole);
                ojt12.setRole(rolesArr3);
                ojt12.setName(ojt2.getCv().getName());
                ojt12.setStatus(accActive);
                userRepo.save(ojt12);
            } else {
                System.out.println("ℹ️ Admin user already exists.");
            }
        };

    }
}
