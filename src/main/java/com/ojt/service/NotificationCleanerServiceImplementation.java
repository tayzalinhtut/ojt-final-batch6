package com.ojt.service;

import com.ojt.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationCleanerServiceImplementation implements NotificationCleanerService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 0 ? * MON")
    @Override
    public void clearOldNotifications() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        notificationRepository.deleteByCreatedAtBefore(cutoff);
        System.out.println("Old notifications cleared.");
    }
}
