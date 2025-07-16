package com.ojt.service;

import com.ojt.dto.OjtProfileDto;
import com.ojt.dto.StaffProfileDto;
import com.ojt.entity.OJT;
import com.ojt.entity.SystemUsers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SystemUserService {
	public void createSystemUser(StaffProfileDto form);
	 public void updateStaffProfile(Long user_id,StaffProfileDto form);
	 public void updateOjtProfile(Long user_id, OjtProfileDto form);
	 public void saveStaffFromExcel(MultipartFile file);

	void convertOjtIntoInactive(Long ojt_id);

}
