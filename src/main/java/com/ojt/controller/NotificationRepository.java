package com.ojt.controller;

import com.ojt.entity.Notification;
import org.springframework.data.repository.Repository;

interface NotificationRepository extends Repository<Notification, Long> {
}
