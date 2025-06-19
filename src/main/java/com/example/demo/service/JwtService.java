package com.example.demo.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${jwt.secret}")
	private String jwtSecret;

	private final long EXP_TIME = 15 * 60 * 1000; // 15 分鐘

	public String createToken(User user) {
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

		return Jwts.builder().setSubject(user.getEmail()).claim("id", user.getId()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXP_TIME)).signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	// 驗證連結 - token
	public String getEmailFromToken(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			return claims.getSubject(); // email
		} catch (Exception e) {
			throw new RuntimeException("Token 無效或已過期");
		}
	}

	public String parseToken(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

			return claims.getSubject();
		} catch (JwtException e) {
			e.printStackTrace(); // 👈 看看到底是哪裡錯
			throw new RuntimeException("Token 解析失敗");
		}
	}
}
