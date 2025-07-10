package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
