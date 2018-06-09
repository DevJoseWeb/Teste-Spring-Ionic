package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nelioalves.cursomc.security.JWTAuthenticationFilter;
import com.nelioalves.cursomc.security.JWTAuthorizationFilter;
import com.nelioalves.cursomc.security.JWTUtil;

/* quando o programa roda, é executado as configuracoes, securityconfig, jacksonconfig, profiletestconfig...
 * libera e bloqueia alguns endpoints, libera o profile de test, desabilita a protecao de ataque csrf e
 * adiciona o filtro quando fizer a requisicao p login.
 * - UserDatailsService: pesquisa o usuario pelo email e retorna
 * - JWTUtil: gera o token usando a palavra secreta, o tempo de expiracao e o username informado
 * - UserSS: ira receber os dados da pesquisa pelo email do usuario que esta autenticando
 * - CredenciaisDTO: quando for autenticar, sera jogado os dados da requisicao nessa classe
 * - JWTAuthenticationFilter: quando é feita a requisicao pro login, ele intercepta, verifica se 
 * o usuario existe, se existir, ele pega o email do usuario e gera o token*/

@Configuration
@EnableWebSecurity
//permite usar anotacoes de pre autorizacao nos endpoints
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final String[] PUBLIC_MATCHES = {
			"/h2-console/**", // tudo q vier depois de h2-console, sera liberado
			"/estados/**"
	};
	
	//permite a leitura dos dados e n a modificacao
	private static final String[] PUBLIC_MATCHES_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	//um cliente tem autorizacao para se cadastrar
	private static final String[] PUBLIC_MATCHES_POST = {
			"/clientes",
			"/auth/forgot/**"
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
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHES_POST).permitAll()
			//so permite o metodo get q tiver na lista
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHES_GET).permitAll()
			//permite todos os caminhos q estao no vetor
			.antMatchers(PUBLIC_MATCHES).permitAll()
			// o resto precisa de autenticacao
			.anyRequest().authenticated();
		
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
		//asegurar q o backend n vai criar uma sessao de usuario
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		//vai usar a instancia de userD.. p dizer quem vai ser capaz de buscar um usuario por email
		//mecanismo de autenticacao ja esta configurado abaixo. quem é o service e o encoder
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	 /*caso queira q requisicoes de multiplas fontes sejam feitas no backend, tem 
	 q liberar explicitamente, por padrao n é liberada; 
	 
	 ta dando acesso basico de multiplas fontes para todos os caminhos, permite 
	 o acesso aos endpoints por multiplas fontes com as configuracoes basicas;
	 
	 é necessario fazer isso, pois estamos em ambiente de testes, vamos usar o aplicativo;*/
	 @Bean
	 CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	 }
	 
	 @Bean
	 BCryptPasswordEncoder bCryptPasswordEncoder() {
		 return new BCryptPasswordEncoder();
	 }
}
