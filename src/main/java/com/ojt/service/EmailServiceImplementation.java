package com.ojt.service;

import com.ojt.dto.OJTDTO;
import com.ojt.entity.CV;
import com.ojt.entity.EmailReply;
import com.ojt.entity.Status;
import com.ojt.entity.User;
import com.ojt.enumeration.StatusType;
import com.ojt.event.EmailEvent;
import com.ojt.repository.CVRepository;
import com.ojt.repository.EmailReplyRepository;
import com.ojt.repository.StatusRepository;
import com.ojt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class  EmailServiceImplementation implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private EmailReplyRepository emailReplyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<OJTDTO> getEligibleRecipients(StatusType cvStatusType, StatusType emailStatusType) {
        List<CV> candidates = cvRepository.findByStatus_StatusType(cvStatusType);

        return candidates.stream()
                .filter(cv -> cv.getEmailReplies().stream()
                        .noneMatch(reply -> reply.getStatus().getStatusType().equals(emailStatusType)))
                .map(cv -> {
                    OJTDTO dto = new OJTDTO();
                    dto.setCvId(cv.getId());
                    dto.setName(cv.getName());
                    dto.setEmail(cv.getEmail());
                    dto.setPhone(cv.getPhone());
                    dto.setStatusId(cv.getStatus() != null ? cv.getStatus().getId() : null);
                    dto.setStatusName(cv.getStatus() != null ? cv.getStatus().getStatusType() : null);
                    dto.setBatchId(cv.getBatch() != null ? cv.getBatch().getId() : null);
                    dto.setBatchName(cv.getBatch() != null ? cv.getBatch().getName() : null);
                    return dto;
                })
                .toList();
    }


    @Override
    public void sendEmailToSelectedRecipients(List<String> recipientEmails, String subject, String body,
                                              StatusType emailStatusType, String senderName) {
        User sender = userRepository.findByStaffName(senderName);
        System.out.println("Sender: " + sender.getStaffName());
        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }
        System.out.println("Test test test test");
        Status emailStatus = statusRepository.findByStatusType(emailStatusType);
        if (emailStatus == null) {
            throw new RuntimeException("Email status not found");
        }

        List<EmailReply> duplicates = emailReplyRepository.findDuplicateEmails(emailStatusType, subject,
                recipientEmails);

        if (!duplicates.isEmpty()) {
            throw new RuntimeException("This email has already been sent to one or more selected recipients.");
        }

        System.out.println("Receipent emails: " + recipientEmails);

        for (String email : recipientEmails) {
            sendEmail(email, subject, body);
        }

        EmailReply reply = new EmailReply();
        reply.setSubject(subject);
        reply.setCreated_at(LocalDate.now());
        reply.setSentBy(sender);
        reply.setStatus(emailStatus);

        List<CV> cvList = cvRepository.findByEmailIn(recipientEmails);
        reply.setCvList(cvList);

        for (CV cv : cvList) {
            if (cv.getEmailReplies() == null) {
                cv.setEmailReplies(new ArrayList<>());
            }
            cv.getEmailReplies().add(reply);
        }

        emailReplyRepository.save(reply);
        cvRepository.saveAll(cvList);
    }

    /*
    private void sendEmail(String toEmail, String subject, String body) {
        System.out.println(subject);
        System.out.println(body);
        System.out.println("To email: " + toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("moteseinlar1@gmail.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
    */

    private void sendEmail(final String toEmail, final String subject, final String body) {
        this.eventPublisher.publishEvent(new EmailEvent(this, toEmail, subject, body));
    }

}
