package com.ojt.service;

import com.ojt.dto.OjtProfileDto;
import com.ojt.dto.StaffProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface SystemUserService {
	public void createSystemUser(StaffProfileDto form);
	 public void updateStaffProfile(Long user_id,StaffProfileDto form);
	 public void updateOjtProfile(Long user_id, OjtProfileDto form);
	 public void saveStaffFromExcel(MultipartFile file);
	 
}
