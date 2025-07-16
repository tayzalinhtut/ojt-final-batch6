package com.ojt.service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.management.relation.Role;

import com.ojt.entity.SystemUsers;
import com.ojt.repository.RoleRepository;
import com.ojt.repository.SystemUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import jakarta.transaction.Transactional;

@Service
public class UserAccService {
	@Autowired
	private SystemUsersRepository userAccRepository;
	
@Autowired
	private RoleRepository roleRepository;


@Autowired
private PasswordEncoder passwordEncoder;

public boolean changePassword(String email, String currentPassword, String newPassword) {
    Optional<SystemUsers> userOpt = userAccRepository.findByEmail(email);
    if (userOpt.isPresent()) {
        SystemUsers user = userOpt.get();

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userAccRepository.save(user);
            return true;
        }
    }
    return false;
}
}



	






		

		






