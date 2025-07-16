package com.ojt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Optional<Role> findById(Long id );
}
