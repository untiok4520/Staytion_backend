package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;
import com.google.api.client.util.Value;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

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

	

	// Google 登入回傳的資料
	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		return principal.getAttributes();
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

	// 接收 Firebase 登入後的 ID token
	@PostMapping("/firebase-login")
	public ResponseEntity<?> loginWithFirebase(@RequestBody Map<String, String> payload) {
		String idToken = payload.get("idToken");

		try {
			FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
			String email = decodedToken.getEmail();

			// 查資料庫是否有這個 email
			Optional<User> optionalUser = userRepository.findByEmail(email);
			User user;

			if (optionalUser.isPresent()) {
				user = optionalUser.get(); // 登入用戶
			} else {
				// 如果沒有，就自動註冊新用戶（第三方登入流程）
				user = new User();
				user.setEmail(email);
				user.setCreatedAt(LocalDateTime.now());
				userRepository.save(user);
			}

			// 發自己的 JWT token
			String jwt = jwtService.createToken(user);

			return ResponseEntity.ok(Map.of("token", jwt, "user", user));

		} catch (FirebaseAuthException e) {
			return ResponseEntity.status(401).body("Firebase 驗證失敗：" + e.getMessage());
		}
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

}
