package com.ojt.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultPasswordService {
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Value("${staff.default.password.pattern}")
    private String staffPasswordPattern;
    
    @Value("${ojt.default.password.pattern}")
    private String ojtPasswordPattern;
    
    @Value("${security.password.min-length:8}")
    private int minPasswordLength;
    
    @Value("${security.password.require-uppercase:true}")
    private boolean requireUppercase;
    
    @Value("${security.password.require-special-char:true}")
    private boolean requireSpecialChar;
    
    
    
    public String generateStaffPassword(String staffId) {
    	 if (staffId == null || staffId.isEmpty()) {
             throw new IllegalArgumentException("Staff ID မထည့်ထားပါ");
         }
    	String password= String.format(staffPasswordPattern,staffId);
    	 System.out.println("ဖန်တီးလိုက်တဲ့ Password: " + password);
    	 return password;
    }
    public String generateOjtPassword() {
        int datePart = LocalDate.now().getYear();
        return String.format(ojtPasswordPattern, datePart);
       
    }
    
    public String encodePassword(String rawPassword) {
    	if (rawPassword == null) {
            throw new IllegalArgumentException("Password မရှိပါ");
        }
        String encode= passwordEncoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encode);
        return encode;
    }
   
}
