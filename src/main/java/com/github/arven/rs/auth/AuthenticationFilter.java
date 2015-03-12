/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

import com.google.common.io.BaseEncoding;
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

/**
 *
 * @author brian.becker
 */
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    
    @Inject
    UserService svc;
    
    public static class UserPrincipal implements Principal {
        
        private final String user;

        public UserPrincipal(String user) {
            this.user = user;
        }

        @Override
        public String getName() {
            return this.user;
        }

        public static UserPrincipal fromHeaders(UserService svc, HttpServletRequest req) {
            if(req.getHeader("Authorization") == null) {
                return new UserPrincipal("anonymous");
            } else {
                String s[] = new String(BaseEncoding.base64().decode(req.getHeader("Authorization").split(" ")[1])).split(":");
                HashedUserInfo info = svc.loadUserByUsername(s[0]);
                if(info.checkPassword(s[1])) {
                    return new UserPrincipal(s[0]);
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
            UserPrincipal p = UserPrincipal.fromHeaders(svc, req);
            req.login(p.getName(), p.getName());
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
    
}
