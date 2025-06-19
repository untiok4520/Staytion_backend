package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.service.CustomOAuth2UserService;

@Configuration
public class OAuth2LoginConfig {

	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
		return new CustomOAuth2UserService(); // 註冊我們的 CustomOAuth2UserService
	}
}