package com.ojt.controller;

import com.ojt.entity.Notification;
import com.ojt.repository.NotificationRepository;
import com.ojt.service.NotificationEmitterService;
import com.ojt.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("admin/api/notifications")
public class NotificationApiController {


    @Autowired
    private NotificationEmitterService notificationEmitterService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{clientId}")
    public SseEmitter connect(@PathVariable String clientId) {
        System.out.println("HELLo con");
        System.out.println("clientId: " + clientId);
        SseEmitter emitter = notificationEmitterService.addClient(clientId);

        return emitter;
    }

    @GetMapping("/notifications/{clientId}")
    public ResponseEntity<List<Notification>> getAllNotifications(@PathVariable String clientId) {
        List<Notification> notifications = notificationService.getNotificationsByRole(clientId);
        return ResponseEntity.ok(notifications);
    }
}
