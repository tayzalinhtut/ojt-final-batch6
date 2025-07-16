package com.ojt.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ojt.entity.OJT;
import com.ojt.entity.Status;
import com.ojt.entity.SystemUsers;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.OJTRepository;
import com.ojt.repository.StatusRepository;
import com.ojt.repository.SystemUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ConvertOjtToSystemUserService {
	@Autowired
	private OJTRepository ojtRepository;

	@Autowired
	private SystemUsersRepository systemUserRepository;

	@Autowired
	private DefaultPasswordService passwordService;

	@Autowired
	private StatusRepository statusRepository;
	
	 @Autowired
	   private StatusRepository statusRepo;

	@Transactional
	public SystemUsers convertOjtToSystemUser(Long ojtId) {
		OJT ojt = ojtRepository.findById(ojtId).orElseThrow(() -> new RuntimeException("OJT candidate not found"));

		List<Status> status = statusRepository.findAll();

		SystemUsers newUser = new SystemUsers();
		newUser.setEmail(ojt.getCv().getEmail());
		newUser.setName(ojt.getCv().getName());
		 Status activeStatus = statusRepo.findByStatusType(StatusType.Active);
	        newUser.setStatus(activeStatus);
	        
		String rawPassword = passwordService.generateOjtPassword();
		newUser.setPassword(passwordService.encodePassword(rawPassword));

		newUser.setUserType("OJT");
		newUser.setFirstTimeLogin(true);
		newUser.setCreatedAt(LocalDateTime.now());
		SystemUsers savedUser = systemUserRepository.save(newUser);
		ojtRepository.save(savedUser);
		return savedUser;

	}

}
