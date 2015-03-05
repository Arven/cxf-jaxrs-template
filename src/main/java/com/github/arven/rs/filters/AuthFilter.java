/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AuthFilter implements Filter {

    @Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String httpAuthHeader = httpRequest.getHeader("authorization");
		if (httpAuthHeader != null && httpAuthHeader.startsWith("Login") 
				&& httpRequest.getRemoteUser() == null) {
			httpAuthHeader = httpAuthHeader.replaceFirst("Login ", "");

			String[] credentials = httpAuthHeader.split(":");
			httpRequest.login(credentials[0], credentials[1]);
		}

		chain.doFilter(request, response);
	}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    @Override
    public void destroy() {
        //
    }

}