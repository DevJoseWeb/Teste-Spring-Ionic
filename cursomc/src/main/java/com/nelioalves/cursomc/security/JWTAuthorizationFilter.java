package com.nelioalves.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	/*vai ter q analisar o token p ver se ele é valido. vai ter q extrair 
	o usuario, buscar no banco de dados p ver se ele existe. por isso 
	tera q injetar o userDetailsService*/
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	//intercepta a requisicao e ve se o usuario ta autorizado
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//pega o valor do cabeçalho, authorization q é o token
		String header = request.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			if(auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
	}

	//retorna o token em forma do UsernamePasswor caso seja valido
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if(!jwtUtil.tokenValido(token)) {
			return null;
		}
		String username = jwtUtil.getUsername(token);
		UserDetails user = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}
}
