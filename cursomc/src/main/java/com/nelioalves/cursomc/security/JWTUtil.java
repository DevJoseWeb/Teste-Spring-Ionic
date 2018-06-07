package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

	public boolean tokenValido(String token) {
		//claims armazena as reivindicacoes do token
		Claims claims = getClaims(token);
		if(claims == null) {
			return false;
		}
		String username = claims.getSubject();
		Date expirationDate = claims.getExpiration();
		Date now = new Date(System.currentTimeMillis());
		
		if(username == null || expirationDate == null || !now.before(expirationDate)) {
			return false;
		}
		return true;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if(claims == null) {
			return null;
		}
		return claims.getSubject();
	}
}
