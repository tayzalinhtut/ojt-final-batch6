package com.ojt.controller;

import com.ojt.dto.OJTDTO;
import com.ojt.entity.CV;
import com.ojt.enumeration.StatusType;
import com.ojt.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String emailPage(Model model, @ModelAttribute("message") String message) {

        model.addAttribute("activePage", "email");
        model.addAttribute("message", message);
        return "admin/email/email";
    }

    @GetMapping("/recipients")
    @ResponseBody
    public List<OJTDTO> getRecipients(@RequestParam("cvStatus") StatusType cvStatus,
                                      @RequestParam("emailStatus") StatusType emailStatus) {
        System.out.println("Fetching CVs with status = " + cvStatus + " and emailStatus = " + emailStatus);
        return emailService.getEligibleRecipients(cvStatus, emailStatus);
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam("recipients") List<String> recipientEmails,
                            @RequestParam("subject") String subject, @RequestParam("emailBody") String body,
                            @RequestParam("emailStatusType") StatusType emailStatus, @RequestParam("senderName") String senderName,
                            Model model, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Sending email");
            System.out.println("Sender name: " + senderName);
            System.out.println("Recipient emails: " + recipientEmails);
            emailService.sendEmailToSelectedRecipients(recipientEmails, subject, body, emailStatus, senderName);
            model.addAttribute("successMessage", "Emails sent successfully!");
            redirectAttributes.addFlashAttribute("message", "Email Sent successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email failed!");
            model.addAttribute("errorMessage", "Error sending emails: " + e.getMessage());
        }

        return "redirect:/admin/email";
    }

}
