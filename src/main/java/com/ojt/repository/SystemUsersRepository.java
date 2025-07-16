package com.ojt.repository;

import com.ojt.entity.SystemUsers;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemUsersRepository extends JpaRepository<SystemUsers, Long> {
    public Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = { "role" })
    Optional<SystemUsers> findByEmail(String email);

    @EntityGraph(attributePaths = { "role" })
    Optional<SystemUsers> findByStaffInfo_StaffId(String staffId);

    Optional<SystemUsers> findByResetToken(String resetToken);

    boolean existsByResetToken(String resetToken);

    List<SystemUsers> findByRole_Name(String roleName);

    List<SystemUsers> findByRole_NameIn(List<String> roleNames);

}
