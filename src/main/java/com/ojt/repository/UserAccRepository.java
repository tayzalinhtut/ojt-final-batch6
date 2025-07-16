package com.ojt.repository;

import com.ojt.entity.UserAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserAccRepository extends JpaRepository<UserAcc, Long> {
    /*UserAcc findByStaffName(String staffName);

    Optional<UserAcc> findByEmail(String email);
    Optional<UserAcc> findByStaffId(String staffId);
    Optional<UserAcc> findByResetToken(String resetToken);
    Optional<UserAcc> findTopByOrderByIdDesc();

    boolean existsByEmail(String email);
    boolean existsByResetToken(String resetToken);
    boolean existsByStaffId(String staffId);

    List<UserAcc> findByRole_Name(String roleName);
    List<UserAcc> findByRole_NameIn(List<String> roleNames);*/
}
