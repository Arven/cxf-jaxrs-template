/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.filter;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author brian.becker
 */
@WebFilter(filterName = "AnonymousSecurityFilter")
public class AnonymousSecurityFilter implements Filter {

    public static class AnonymousAuthorizationWrapper extends HttpServletRequestWrapper {
        
        public AnonymousAuthorizationWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if(name.equals("Authorization")) {
                String header = super.getHeader(name);
                return (header != null) ? header : "Basic " + Base64.encode("trfields:snowing".getBytes());
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            if(!names.contains("Authorization")) { names.add("Authorization"); }
            return Collections.enumeration(names);
        }
        
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;        
            final String auth = req.getHeader( "Authorization" );
            if(auth == null) {
                chain.doFilter(new AnonymousAuthorizationWrapper(req), res);
            }
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
