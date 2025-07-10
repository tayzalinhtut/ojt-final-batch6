package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByStaffName(String staffName);
}
