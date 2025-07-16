package com.ojt.service;

import com.ojt.entity.*;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component // This will make this class a Spring-managed bean
public class ExcelHelper {
    @Autowired

    private StatusRepository statusRepo;

    @Autowired

    private RoleRepository roleRepo;

    @Autowired

    private DefaultPasswordService passwordService;

    @Autowired

    private SystemUsersRepository systemUserRepo;

    @Autowired

    private StaffInfoRepository staffInfoRepo;


    @Autowired

    private InstructorRepository instructorRepo;

    @Transactional

    public void excelToStaffList(InputStream is) {

        List<StaffInfo> staffList = new ArrayList<>();

        try {

            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to read the first sheet


            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skipping header row

                Row row = sheet.getRow(i);

                // Row သည် null ဖြစ်နေလျှင် skip လုပ်ပါ

                if (row == null) {

                    continue;

                }


                System.out.println("this is row " + i);

                String staffId = getStringCellValue(row.getCell(0));

                if (staffId == null || staffId.trim().isEmpty()) {

                    System.out.println("Row " + (i + 1) + " ကိုကျော်သွားမည် - Staff ID မရှိပါ");

                    continue;

                } else {


                    StaffInfo staff = new StaffInfo();

                    SystemUsers user = new SystemUsers();

                    staff.setStaffId(getStringCellValue(row.getCell(0))); // Staff ID in column 0


                    System.out.println("This is excel staffId" + getStringCellValue(row.getCell(0)));

                    // Set password

                    String rawPassword = passwordService.generateStaffPassword(getStringCellValue(row.getCell(0)));

                    user.setPassword(passwordService.encodePassword(rawPassword));


                    staff.setDivision(getStringCellValue(row.getCell(1)));

                    System.out.println("This is excel staffId" + getStringCellValue(row.getCell(1)));


                    user.setName(getStringCellValue(row.getCell(2)));


                    staff.setDeptment(getStringCellValue(row.getCell(3)));


                    staff.setTeam(getStringCellValue(row.getCell(4)));

                    user.setEmail(getStringCellValue(row.getCell(5)));


                    staff.setPosition(getStringCellValue(row.getCell(6)));

                    String cellValue = getStringCellValue(row.getCell(7));

                    if (cellValue != null && cellValue.trim().equalsIgnoreCase(StatusType.Active.toString())) {

                        Status activeStatus = statusRepo.findByStatusType(StatusType.Active);

                        user.setStatus(activeStatus);


                        //Status userStatus=statusRepo.findByStatusType(StatusType.Active);

                        System.out.print("This is user true status " + activeStatus);


                        //user.setStatus(userStatus);

                    } else {

                        Status activeStatus = statusRepo.findByStatusType(StatusType.Active);

                        System.out.print("This is user status active  " + activeStatus);

                        System.out.print("This is  excel user status active  " + getStringCellValue(row.getCell(7)));


                        System.out.print("This is user status is Null");

                    }

                    List<Role> rolesArr = new ArrayList<>();

                    if ("Operator".equals(getStringCellValue(row.getCell(8)))) {

                        Role userRole = roleRepo.findByName("Operator");


                        rolesArr.add(userRole);


                    } else if ("Admin".equals(getStringCellValue(row.getCell(8)))) {

                        Role userRole = roleRepo.findByName("Admin");


                        rolesArr.add(userRole);

                    } else {

                        Role userRole = roleRepo.findByName("Instructor");

                        rolesArr.add(userRole);


                    }

                    user.setRole(rolesArr);
                    System.out.println("This is roles " + rolesArr);
                    user.setFirstTimeLogin(true);

                    user.setCreatedAt(LocalDateTime.now());

                    user.setUserType("Staff");


                    systemUserRepo.save(user);

                    List<Role> roles = new ArrayList<>();

                    roles = user.getRole();

                    for (int j = 0; j < roles.size(); j++) {

                        Role role = roles.get(j);
                        System.out.println("This is role " + role);

                        if ("Instructor".equals(role.getName())) {

                            staff.setUser(user);

                            staffInfoRepo.save(staff);

                            // Set bidirectional relationship

                            user.setStaffInfo(staff);

                            systemUserRepo.save(user);


                            //3.Create and save Instructor

                            Instructor instructor = new Instructor();

                            instructor.setStaffInfo(staff);

                            instructorRepo.save(instructor);


                            // Set bidirectional relationship

                            staff.setInstructor(instructor);

                            staffInfoRepo.save(staff);


                        } else {

                            staff.setUser(user);

                            staffInfoRepo.save(staff);

                            // Set bidirectional relationship

                            user.setStaffInfo(staff);

                            systemUserRepo.save(user);

                        }


                    }


                }

            }

            workbook.close();

        } catch (IOException e) {

            e.printStackTrace();


        }


    }


    private static String getStringCellValue(Cell cell) {

        if (cell == null) {

            return null;

        }

        if (cell.getCellType() == CellType.STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellType() == CellType.NUMERIC) {

            return String.valueOf((long) cell.getNumericCellValue()); // Convert numeric to string

        } else {

            return "";

        }

    }


    private static Long getLongCellValue(Cell cell) {

        if (cell == null) {

            return null;

        }

        if (cell.getCellType() == CellType.NUMERIC) {

            return (long) cell.getNumericCellValue(); // Convert numeric value to Long

        } else if (cell.getCellType() == CellType.STRING) {

            try {

                return Long.parseLong(cell.getStringCellValue()); // Convert string to Long

            } catch (NumberFormatException e) {

                return null; // Handle if the string cannot be parsed to a number

            }

        }

        return null;

    }


}