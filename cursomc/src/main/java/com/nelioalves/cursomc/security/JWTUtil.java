package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//p q seja injetada em outras classes como componente
@Component
public class JWTUtil {
			
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String username) {
		return Jwts.builder()
					.setSubject(username)
					.setExpiration(new Date(System.currentTimeMillis() + expiration))
//					como vai assinar o token
					.signWith(SignatureAlgorithm.HS512, secret.getBytes())
					.compact();
	}
}
