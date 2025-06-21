package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.FirebaseService;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FirebaseService firebaseService;

	// Google登入
	@PostMapping("/auth/google-login")
	public ResponseEntity<Map<String, Object>> googleLogin(@RequestBody Map<String, String> googleUserData) {
		String email = googleUserData.get("email");
		String firstName = googleUserData.get("firstName");
		String lastName = googleUserData.get("lastName");

		// 檢查資料庫中是否已有用戶
		Optional<User> userOptional = userRepository.findByEmail(email);
		User user;
		if (userOptional.isPresent()) {
			user = userOptional.get();
		} else {
			user = new User();
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setCreatedAt(LocalDateTime.now());
			userRepository.save(user);
		}

		// 將用戶資料同步到 Firebase
		firebaseService.createFirebaseUser(email, firstName, lastName);

		// 生成 JWT Token
		String token = jwtService.createToken(user);

		return ResponseEntity.ok(Map.of("token", token, "user", user));
	}

	// 檢查email是否存在
	@PostMapping("/auth/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody @Valid EmailRequest request) {
		boolean exists = userRepository.existsByEmail(request.getEmail());
		return ResponseEntity.ok(Map.of("exists", exists));
	}

	// 使用密碼登入
	@PostMapping("/auth/login-password")
	public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest request) {
		return authService.loginWithPassword(request);
	}

	// 使用token取得個人資料
	@GetMapping("/auth/profile")
	public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		System.out.println("Authorization header: " + authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
		}

		String token = authHeader.substring(7);
		System.out.println("Extracted token: " + token);

		String email = jwtService.parseToken(token);
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("找不到使用者"));

		return ResponseEntity.ok(user);
	}

	// 註冊並呼叫Service處理註冊與寄信
	@PostMapping("/auth/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest request) {
		return authService.register(request);
	}

	// 忘記密碼，發送驗證連結
	@PostMapping("/auth/forgot-password")
	public ResponseEntity<?> sendResetPasswordEmail(@RequestBody ForgotPasswordRequest request) {
		return authService.sendResetPasswordEmail(request.getEmail());
	}

	// 重設密碼
	@PostMapping("/auth/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
		return authService.resetPassword(request.getToken(), request.getNewPassword());
	}

	// 更新使用者個人資料
	@PutMapping("/auth/update-profile")
	public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,
			@RequestBody UserUpdateRequest request) {
		String email = jwtService.parseToken(token.substring(7)); // 解析 token 獲取 email
		User updatedUser = userService.updateUserProfile(email, request.getFirstName(), request.getLastName(),
				request.getPassword(), request.getTel());
		return ResponseEntity.ok(updatedUser);
	}
	
	// 刪除使用者
	@DeleteMapping("/auth/delete-account")
	public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token) {
	    String email = jwtService.parseToken(token.substring(7));
	    String message = userService.deleteUser(email);
	    return ResponseEntity.ok(Map.of("message", message));
	}

	
}
