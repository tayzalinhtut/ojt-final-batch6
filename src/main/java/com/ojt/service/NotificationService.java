package com.ojt.service;

import com.ojt.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications();
    List<Notification> getNotificationsByRole(String role);
}
