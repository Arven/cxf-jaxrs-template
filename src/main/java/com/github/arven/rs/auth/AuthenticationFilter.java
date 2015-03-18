/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

import com.github.arven.auth.RolePrincipal;
import com.github.arven.auth.UserPrincipal;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
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
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    
    public static class BasicPrincipal implements Principal {

        @Override
        public String getName() {
            return "user";
        }
        
    }
    
    public static class SecurityClientCallbackHandler implements CallbackHandler
    {
      public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
      {
        //loop over parameter Callbacks
        for (int intIndexCallback = 0; intIndexCallback < callbacks.length; intIndexCallback++)
        {
          //NameCallback: set Login
          if (callbacks[intIndexCallback] instanceof NameCallback)
          {
            NameCallback nameCallback = (NameCallback) callbacks[intIndexCallback];
            nameCallback.setName( "anonymous" );
          }
          //PasswordCallback: set password.
          else if (callbacks[intIndexCallback] instanceof PasswordCallback)
          {
            PasswordCallback passwordCallback = (PasswordCallback) callbacks[intIndexCallback];
            passwordCallback.setPassword ("anonymous".toCharArray() );
          }
          else
          {
            throw new UnsupportedCallbackException (callbacks[intIndexCallback], "Unsupported Callback!");
          }

        }
      }
    }    
    
    public static class WrapAuthentication extends HttpServletRequestWrapper {

        public WrapAuthentication(HttpServletRequest request) {
            super(request);
        }
        
        @Override
        public Principal getUserPrincipal() {
            return new BasicPrincipal();
        }
        
        @Override
        public String getRemoteUser() {
            return this.getUserPrincipal().getName();
        }
        
        @Override
        public String getAuthType() {
            return "BASIC";
        }
        
    }
    
    @Override
    public void init(FilterConfig cfg) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String username = "anonymous", password = "anonymous";
            if(req.getHeader("Authorization") != null) {
                String s[] = (new String(BaseEncoding.base64().decode(req.getHeader("Authorization").split(" ")[1]))).split(":");
                username = s[0]; password = s[1];
            }
            req.logout();
            req.login(username, password);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
    
}
