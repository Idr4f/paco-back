package com.unidapp.manager.api.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void init(FilterConfig fc) throws ServletException {
		logger.info("JWTContamaxx | SimpleCORSFilter loaded");
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Credentials", "false");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, origin, content-type, accept");
		
		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}
	}
	
	@Override
	public void destroy() {
		
	}
}