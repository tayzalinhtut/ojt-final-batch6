package com.ojt.listener;

import com.ojt.event.EmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @EventListener
    public void handleEmailEvent(final EmailEvent event) {
        try {
            final SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("moteseinlar1@gmail.com");
            message.setTo(event.getToEmail());
            message.setSubject(event.getSubject());
            message.setText(event.getBody());
            this.mailSender.send(message);
            System.out.println("Email sent to: " + event.getToEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email to " + event.getToEmail() + ": " + e.getMessage());
        }
    }
}