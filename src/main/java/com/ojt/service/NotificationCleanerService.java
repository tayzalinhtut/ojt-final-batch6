package com.ojt.service;

import com.ojt.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

public interface NotificationCleanerService {
    @Scheduled(fixedRate = 86400000)
    void clearOldNotifications();
}
