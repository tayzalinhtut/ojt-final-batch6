package com.ojt.service;

import com.ojt.entity.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public interface NotificationEmitterService {
    SseEmitter addClient(String clientId);
    void broadcastToAdmin(Notification notification);
}
