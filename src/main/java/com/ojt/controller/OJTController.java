package com.ojt.controller;

import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.ojt.dto.OJTCustomDTO;
import com.ojt.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ojt.dto.OJTDTO;
import com.ojt.entity.OJT;
import com.ojt.enumeration.StatusType;
import com.ojt.service.OJTService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class OJTController {

    @Autowired
    private OJTService ojtService;

    @Autowired
    private AttendanceService atattendanceService;

    @GetMapping("/ojt-members")
    public String showOjtMembers(Model model, @ModelAttribute("message") String message) {
        List<OJT> allOjts = ojtService.getAllOJT();

        List<OJTCustomDTO> data = new ArrayList<>();

        for(OJT ojt : allOjts) {
            Long ojtId = ojt.getId();
            Long batchId = ojt.getCv().getBatch().getId();

            OJTDTO ojtDto = new OJTDTO();

            ojtDto.setName(ojt.getCv().getName());
            ojtDto.setBatchName(ojt.getCv().getBatch().getName());
            ojtDto.setStatusName(ojt.getStatus().getStatusType());
            ojtDto.setId(ojt.getId());
            ojtDto.setBankAccount(ojt.getBankAccount());
            ojtDto.setCvId(ojt.getCv().getId());
            ojtDto.setBatchId(ojt.getCv().getBatch().getId());
            ojtDto.setStatusId(ojt.getStatus().getId());
            ojtDto.setEmail(ojt.getCv().getEmail());

            int attendance = (int) atattendanceService.calculatedAttendancePercentage(ojtId, batchId);
            System.out.println("Attendance : " + attendance);

            data.add(new OJTCustomDTO(ojtDto, attendance));
        }

        model.addAttribute("ojts", data);
        model.addAttribute("message", message);

        return "admin/ojt/ojt-member-management";
    }

    @GetMapping("/ojt-members/{id}")
    public String showOjtMember(@PathVariable("id") Long id, Model model, Principal principal) {
        OJT ojtMember = ojtService.getOJTById(id);

        model.addAttribute("ojt", ojtMember);

        long monthsBetween = ChronoUnit.MONTHS.between(ojtMember.getCv().getBatch().getStartDate(), ojtMember.getCv().getBatch().getEndDate());
        model.addAttribute("monthsBetween", monthsBetween);


        return "admin/ojt/ojt-member-profile-details";
    }

    @GetMapping("/ojt-members/edit/{id}")
    public String showOJTEdit(Model model, @PathVariable("id") Long id) {
        OJT ojt = ojtService.getOJTById(id);
        OJTDTO ojtDto = new OJTDTO();

        ojtDto.setName(ojt.getCv().getName());
        ojtDto.setBatchName(ojt.getCv().getBatch().getName());
        ojtDto.setStatusName(ojt.getStatus().getStatusType());
        ojtDto.setId(ojt.getId());
        ojtDto.setBankAccount(ojt.getBankAccount());
        ojtDto.setCvId(ojt.getCv().getId());
        ojtDto.setBatchId(ojt.getCv().getBatch().getId());
        ojtDto.setStatusId(ojt.getStatus().getId());
        ojtDto.setEmail(ojt.getCv().getEmail());
        ojtDto.setPhone(ojt.getCv().getPhone());


        model.addAttribute("ojtDto", ojtDto);

        return "admin/ojt/ojt-member-profile-edit";
    }

    @PostMapping("/ojt-members/edit/{id}")
    public String editOJT(@PathVariable("id") Long id,@Valid @ModelAttribute("ojtDto") OJTDTO ojtDto, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            System.out.println("Has Error");
            return "admin/ojt/ojt-member-profile-edit";
        }

        ojtService.updateOJT(id, ojtDto);

        redirectAttributes.addFlashAttribute("message", "OJT successfully updated!!");

        return "redirect:/admin/ojt-members";
    }

    @GetMapping("/ojt-members/delete")
    public String delteResource(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) throws Exception {
        ojtService.deleteOJT(id);

        redirectAttributes.addFlashAttribute("message", "OJT successfuly deleted!");

        return "redirect:/admin/resources";
    }

    @GetMapping("/ojt-members/test-exception")
    public String testException(RedirectAttributes redirectAttributes) throws Exception {
        throw new Exception("Test: OJT Member Not Found!");
    }

}