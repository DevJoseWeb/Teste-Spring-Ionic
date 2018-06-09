package com.nelioalves.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.dto.CredenciaisDTO;

/* filtro q vai interceptar a requisicao de login, verificar as credenciais 
   e vai autenticar ou n o usuario gerando, o token jwt em caso de autenticacao.
   -autorizacao é autorizar certos recursos p o usuario autenticado.
   -autenticacao é verificar se o usuario é ele mesmo.
   -o usuario ira informar as credenciais, email e senha e o sistema ira dizer 
   se as credenciais sao validas ou nao. se for valida sera gerado um token para 
   a pessoa. o token jwt é um codigo q contem usuario e o tempo de expiracao do 
   token. o token sera assinado com uma palavra secreta q sera informado.
 */
/* spring security oferece implementacoes*/
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	//
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	//
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {

		try {
			//pega os dados q veio da requisicao e converte p credenciais dto
			CredenciaisDTO creds = new ObjectMapper().readValue(req.getInputStream(), CredenciaisDTO.class);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(),
					creds.getSenha(), new ArrayList<>());

			//vai verificar se o usuario e senha sao validos baseado no q implementou no userdetailservice. 
			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//se a autenticacao der certo, tem q gerar um token e adicionar na resposta da requisicao
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		//getprincipal retorna o usuario do spring s
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		res.addHeader("Authorization", "Bearer " + token);
		//naturalmente a aplicacao n consegue acessar, entao tem q deixar exposto.
		//esta enviando um cabecalho personalizado q é o authorization, entao tem q dar uma instrucao explicita no backend p liberar a leitura do cabecalho
		res.addHeader("access-control-expose-headers", "Authorization");
		//acrescentando um cabecalho customizado, por padrao n é aceito pelo mecanismo de cors
		/* quais recursos (metodo http, ou header) estarao disponiveis para requisicoes advindas de origens diferentes
		 * a api/backend esta hospedado em um certo servidor, quando vem uma requisicao de outro endereco, o q vai disponibilizar o cr q esta fazendo a requisicao p mim. o mecanismo q define isso é o cors
		 * - a requisicao de outra origem, ele é get?
		 * - get e tem cabeçalho customizado, vai p options, senao ele retorna a pagina.
		 * - options é outro verbo http, ele verifica se é permitido acessar algo, se o controle de acesso estiver tudo bem, ele devolve o recurso, senao da erro de cors
		 * - se for post e content-type n for padrao, ele vai p options, senao, ele ve se o cabecalho é customizado se for, vai p options, senao retorna pagina
		 * - se for put ou delete ele vai fazer a verificacao adicional, pois eles sao metodos mais graves, pq ele altera o recurso*/
		
		//configura o cors no bucket
	}

	/*
	 * codigo 403 é n autorizado. codigo de autenticacao quando as credenciais estao
	 * erradas, o codigo é 401. classe corrige erro do spring boot 2.
	 */
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
		}

		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Não autorizado\", "
					+ "\"message\": \"Email ou senha inválidos\", " + "\"path\": \"/login\"}";
		}
	}
}