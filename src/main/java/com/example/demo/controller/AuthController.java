package com.example.demo.controller;

import java.util.Map;

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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;
	
	// Google 登入
    @PostMapping("/auth/google-login")
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestBody Map<String, String> googleUserData) {
        String email = googleUserData.get("email");
        String firstName = googleUserData.get("firstName");
        String lastName = googleUserData.get("lastName");

        return authService.handleGoogleLogin(email, firstName, lastName);
    }
	

	// 檢查email是否存在
	@Operation(summary = "檢查電子郵件是否已註冊", description = "提供電子郵件來檢查該郵箱是否已經註冊過")
	@PostMapping("/auth/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody @Valid EmailRequest request) {
		boolean exists = userRepository.existsByEmail(request.getEmail());
		return ResponseEntity.ok(Map.of("exists", exists));
	}

	// 使用密碼登入
	@Operation(summary = "使用密碼登入", description = "根據使用者提供的帳號和密碼進行登入，成功後返回 JWT Token")
	@PostMapping("/auth/login-password")
	public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest request) {
		return authService.loginWithPassword(request);
	}

	// 註冊並呼叫Service處理註冊與寄信
	@Operation(summary = "註冊新用戶", description = "提供註冊所需資料(email、password、lastName、firstName)並創建新用戶，註冊成功會發送確認郵件")
	@PostMapping("/auth/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest request) {
		return authService.register(request);
	}

	// 忘記密碼，發送驗證連結
	@Operation(summary = "忘記密碼 - 發送重設密碼郵件", description = "用戶輸入註冊時的郵件地址，系統會發送重設密碼的郵件。")
	@PostMapping("/auth/forgot-password")
	public ResponseEntity<Map<String, String>> sendResetPasswordEmail(@RequestBody ForgotPasswordRequest request) {
		return authService.sendResetPasswordEmail(request.getEmail());
	}

	// 重設密碼
	@Operation(summary = "重設密碼", description = "用戶提供重設密碼的 Token 和新密碼，進行密碼重設。")
	@PostMapping("/auth/reset-password")
	public ResponseEntity<Map<String, String>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
		return authService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
	}

}
