package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 更新個人資料
	public User updateUserProfile(String email, String firstName, String lastName, String password, String tel) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("找不到使用者"));

		// 更新資料
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPassword(passwordEncoder.encode(password));
		user.setTel(tel);

		// 保存更新後的資料
		return userRepository.save(user);
	}
	
	// 刪除使用者資料
	public String deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        // 刪除用戶
        userRepository.delete(user);
        return "用戶已刪除";
    }
}
