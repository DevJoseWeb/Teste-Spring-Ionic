package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private Environment env;
	
	private static final String[] PUBLIC_MATCHES = {
			"/h2-console/**", // tudo q vier depois de h2-console, sera liberado
	};
	
	//permite a leitura dos dados e n a modificacao
	private static final String[] PUBLIC_MATCHES_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//se no profile ativo for o test, significa que vai ser utilizado o h2
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			//libera o acesso ao test e consequentemente ao h2
			http.headers().frameOptions().disable();
		}
		//para q o bean do cors seja ativado
		http.cors()
			//desabilita a protecao de ataque csrf pois o projeto é stateless. ataque é baseado no armazenamento de autenticacao em sessao
			.and().csrf().disable();
		
		http.authorizeRequests()
			//so permite o metodo get q tiver na lista
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHES_GET).permitAll()
			//permite todos os caminhos q estao no vetor
			.antMatchers(PUBLIC_MATCHES).permitAll()
			// o resto precisa de autenticacao
			.anyRequest().authenticated();
		//asegurar q o backend n vai criar uma sessao de usuario
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	 /*caso queira q requisicoes de multiplas fontes sejam feitas no backend, tem 
	 q liberar explicitamente, por padrao n é liberada; 
	 
	 ta dando acesso basico de multiplas fontes para todos os caminhos, permite 
	 o acesso aos endpoints por multiplas fontes com as configuracoes basicas;
	 
	 é necessario fazer isso, pois estamos em ambiente de testes, vamos usar o aplicativo;*/
	 @Bean
	 CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	 }
	 
	 @Bean
	 BCryptPasswordEncoder bCryptPasswordEncoder() {
		 return new BCryptPasswordEncoder();
	 }
}
