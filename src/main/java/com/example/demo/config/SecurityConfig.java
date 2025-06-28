package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())   // 關閉 CSRF 防護，因為 API 可能會在前端直接調用
//            .cors(cors -> {})                // 啟用 CORS（配合 WebMvcConfigurer）
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/auth/google-login", "/api/auth/login-password", "/api/auth/register", "/api/auth/check-email", "/api/top-hotels", "/api/city/{id}/hotel-count", "/api/auth/forgot-password", "/api/auth/reset-password", "/api/user/profile", "/user/profile").permitAll() // 允許無需驗證的路徑
//                .anyRequest().authenticated()  // 其他所有請求需進行驗證
//            )
//            .oauth2Login(oauth2 -> oauth2
//                .userInfoEndpoint(userInfo -> userInfo // 配置 OAuth2 用戶端
//                    .userService(new DefaultOAuth2UserService())  // 使用預設的 OAuth2UserService
//                )
//            );
//        
//        return http.build();
//    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // 關閉 CSRF 防護，通常 API 會這樣做
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 允許所有路徑匿名訪問
		return http.build(); // 最後需要返回 http.build() 來生成 SecurityFilterChain 物件
	}

	// WebMvcConfigurer 設定允許跨網域 + credentials
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500"," https://baking-suites-on-myers.trycloudflare.com") // 允許前端的域名（根據實際需要修改）
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
						.allowedHeaders("*").allowCredentials(true); // 允許跨域請求攜帶憑證（如 cookies）
			}
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // 用於處理密碼的加密
	}
}
