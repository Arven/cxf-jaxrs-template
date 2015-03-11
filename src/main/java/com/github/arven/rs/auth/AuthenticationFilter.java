/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author brian.becker
 */
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    
    public static class SecurityWrapper extends HttpServletRequestWrapper {
        
        @Inject
        UserService svc;
        
        private final UsernamePasswordPrincipal p;

        public SecurityWrapper(HttpServletRequest request) {
            super(request);
            this.p = UsernamePasswordPrincipal.fromHeaders(svc, request);
        }
        
        @Override
        public Principal getUserPrincipal() {
            return this.p;
        }
        
        @Override
        public boolean isUserInRole(String role) {
            return this.p.isUserInRole(role);
        }
        
    }
    
    public static class UsernamePasswordPrincipal implements Principal {
        
        private final String user;
        private final String password;
        private final Collection<String> roles;

        public UsernamePasswordPrincipal(String user, String pass, Collection<String> roles) {
            this.user = user;
            this.password = pass;
            this.roles = roles;
        }

        @Override
        public String getName() {
            return this.user;
        }

        @Override
        public String toString() {
            return Base64.encode((this.user + ":" + this.password).getBytes()) + " " + this.roles.toString();
        }
        
        public boolean isUserInRole(String role) {
            return this.roles.contains(role);
        }

        public static UsernamePasswordPrincipal fromHeaders(UserService svc, HttpServletRequest req) {
            if(req.getHeader("Authorization") == null) {
                return new UsernamePasswordPrincipal("anonymous", "", Arrays.asList(new String[] { "ANONYMOUS" }));
            } else {
                String s[] = new String(Base64.decode(req.getHeader("Authorization").split(" ")[1])).split(":");
                HashedUserInfo info = svc.loadUserByUsername(s[0]);
                if(info.checkPassword(s[1])) {
                    return new UsernamePasswordPrincipal(s[0], s[1], info.getRoles());
                }
            }
            throw new RuntimeException("Authentication failed");
        }
        
    }
    
    @Override
    public void init(FilterConfig cfg) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            chain.doFilter(new SecurityWrapper(req), response);
        }
    }

    @Override
    public void destroy() {
    }
    
}
