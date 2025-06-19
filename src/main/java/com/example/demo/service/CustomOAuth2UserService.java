package com.example.demo.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		// 通過 OAuth2UserRequest 取得 Google 的用戶資料
		OAuth2User oauth2User = new DefaultOAuth2User(AuthorityUtils.createAuthorityList("ROLE_USER"),
				userRequest.getAdditionalParameters(), "email" // 這裡指定了作為主屬性的欄位，通常是 email
		);

		// 從 oauth2User 的屬性中提取資料
		String email = oauth2User.getAttribute("email");
		String firstName = oauth2User.getAttribute("given_name");
		String lastName = oauth2User.getAttribute("family_name");

		// 將資料返回，以便在後端進行處理（例如註冊或登入）
		Map<String, Object> attributes = oauth2User.getAttributes();
		attributes.put("email", email);
		attributes.put("firstName", firstName);
		attributes.put("lastName", lastName);

		// 返回新的 OAuth2User 實例，並附加上我們的自定義資料
		return new DefaultOAuth2User(AuthorityUtils.createAuthorityList("ROLE_USER"), attributes, "email");
	}
}
