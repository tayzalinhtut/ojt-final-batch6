package com.ojt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ojt.entity.Role;
import com.ojt.entity.StaffInfo;
import com.ojt.entity.SystemUsers;
import com.ojt.repository.StaffInfoRepository;
import com.ojt.repository.SystemUsersRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private SystemUsersRepository userAccRepository;
	@Autowired
	private StaffInfoRepository staffRepo;

	@Override
	public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {
		System.out.println("👉 Login input: " + loginInput);

		Optional<SystemUsers> userOptional;
		Optional<StaffInfo> staffOptional;
		SystemUsers user = new SystemUsers();
		if (loginInput.contains("@")) {
			System.out.println("🔍 Trying with Email...");
			userOptional = userAccRepository.findByEmail(loginInput);
			System.out.print("This user is trying  " + userOptional);
			if(userOptional.get() != null) {
				user = userOptional.get();
			}
		} else {
			System.out.println("🔍 Trying with StaffId...");
			staffOptional = staffRepo.findByStaffId(loginInput);
			user = staffOptional.get().getUser();

		}

		System.out.println("the password is " + user.getPassword());
		for(Role r : user.getRole()) {
			System.out.println("user role: " + r.getName());
		}
		List<SimpleGrantedAuthority> authorities = user.getRole().stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
		System.out.println("Authority: " + authorities);
		System.out.println("Loginn input: " + loginInput);
		System.out.println("User email: " + user.getEmail());

		return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()).authorities(authorities).build();

		// User(withUsername(user.getEmail() or user.getStaffId())
		// .password(user.getPassword())

	}
}
