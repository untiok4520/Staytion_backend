package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.google.api.client.util.Value;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@Autowired
	private JwtService jwtService;

	// 檢查 email 是否存在
	public ResponseEntity<String> checkEmail(String email) {

		if (userRepository.existsByEmail(email)) {
			return ResponseEntity.ok("EXISTS");
		} else {
			// 帳號不存在 => 跳轉到註冊頁面
			return ResponseEntity.status(404).body("NOT_FOUND");
		}
	}

	// 使用email+密碼登入
	public ResponseEntity<Map<String, String>> loginWithPassword(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("帳號不存在"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("密碼錯誤");
		}

		// 檢查 Firebase 上的 email 是否已驗證
		try {
			UserRecord firebaseUser = FirebaseAuth.getInstance().getUserByEmail(request.getEmail());
			if (!firebaseUser.isEmailVerified()) {
				throw new RuntimeException("您的電子郵件尚未驗證。請查收驗證郵件。");
			}
		} catch (Exception e) {
			throw new RuntimeException("檢查 Firebase 驗證狀態時發生錯誤: " + e.getMessage());
		}

		String token = jwtService.createToken(user);

		return ResponseEntity.ok(Map.of("token", token));
	}

	// 註冊
	public ResponseEntity<Map<String, String>> register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email 已註冊");
		}

		// 1. 儲存本地用戶
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setTel(request.getTel());
		user.setCreatedAt(LocalDateTime.now());
		userRepository.save(user);

		try {
			// 2. 在 Firebase 建立帳號
			CreateRequest firebaseRequest = new CreateRequest().setEmail(request.getEmail())
					.setPassword(request.getPassword());

			UserRecord firebaseUser = FirebaseAuth.getInstance().createUser(firebaseRequest);
			System.out.println("Firebase user created: " + firebaseUser.getUid());

			// 3. 產生驗證連結並寄信
			String link = FirebaseAuth.getInstance().generateEmailVerificationLink(request.getEmail());
			String html = "<p>請點擊以下連結完成帳號驗證：</p><a href=\"" + link + "\">驗證帳號</a>";
			mailService.sendHtmlMail(request.getEmail(), "帳號驗證信", html);

		} catch (Exception e) {
			throw new RuntimeException("Firebase 建立用戶或發送驗證信失敗: " + e.getMessage());
		}

		return ResponseEntity.ok(Map.of("message", "註冊成功，驗證郵件已發送"));
	}

	// 用於處理 Google 登入後的用戶資料
	public ResponseEntity<Map<String,Object>> handleGoogleLogin(String email, String firstName, String lastName) {
		// 查詢資料庫是否已存在該用戶
		Optional<User> existingUser = userRepository.findByEmail(email);

		if (existingUser.isPresent()) {
			// 如果用戶已存在，直接返回用戶資料
			return ResponseEntity.ok(Map.of("message", "User already exists", "user", existingUser.get()));
		}

		// 如果用戶不存在，創建新用戶
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setCreatedAt(LocalDateTime.now()); // 使用當前時間來創建用戶
		userRepository.save(newUser);
		
		// 發自己的 JWT token
		String jwt = jwtService.createToken(newUser);

		return ResponseEntity.ok(Map.of("message", "New user created", "user", newUser, "token", jwt));
	}

	// 發送重設密碼連結
	public ResponseEntity<?> sendResetPasswordEmail(String email) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("該 email 不存在");
		}

		User user = userOpt.get();
		String token = jwtService.createToken(user); // JWT: 含 email、15 分鐘有效
		String resetLink = "http://localhost:8080/reset-password.html?token=" + token;

		String html = "<p>請點擊以下連結重設密碼：</p><a href=\"" + resetLink + "\">重設密碼</a>";
		mailService.sendHtmlMail(email, "重設密碼", html);

		return ResponseEntity.ok("重設密碼連結已寄出");
	}

	// 重設密碼
	public ResponseEntity<?> resetPassword(String token, String newPassword) {
		try {
			String email = jwtService.getEmailFromToken(token);
			User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("找不到使用者"));

			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);

			return ResponseEntity.ok("密碼已更新");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token 錯誤或已過期");
		}
	}

}
