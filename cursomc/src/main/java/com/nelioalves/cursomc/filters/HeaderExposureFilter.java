package com.nelioalves.cursomc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class HeaderExposureFilter implements Filter{

	//executa quando o filtro for criado
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//como o back end é rest, entao com certeza o servletresponse é httpservletresponse, dara p fazer a conversao
		HttpServletResponse res = (HttpServletResponse) response;
		//expor explicitamente o cabecalho location
		res.addHeader("access-control-expose-headers", "location");
		//continua a requisicao normalmente. encaminha p seu ciclo normal.
		chain.doFilter(request, response);
	}

	//executa quando o filtro for destruido
	@Override
	public void destroy() {
	}

}
