/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.filter;

import com.google.common.base.Charsets;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang.StringUtils;

//@WebFilter(filterName = "Jsr250SecurityFilter")
public class Jsr250SecurityFilter implements Filter  {
    
    public static final String _user = "trfields";
    public static final String _password = "snowing";
    public static final String _realm = "filter";  
    
    public static class Pair<A, B> {
        public A a;
        public B b;
        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }
    
    Map<String, Pair<Method, Annotation>> matcher = new HashMap<String, Pair<Method, Annotation>>();
        
    public static String getPathOrDefault(AnnotatedElement ae, String def) {
        for(Annotation a : ae.getDeclaredAnnotations()) {
            if(a instanceof Path) {
                Path p = (Path) a;
                return p.value();
            }
        }
        return def;
    }
    
    public Annotation getPermissionsForPath(String request) {
        for(Entry<String, Pair<Method, Annotation>> ent : matcher.entrySet()) {
            String match = ent.getKey();
            for(Annotation[] as : ent.getValue().a.getParameterAnnotations()) {
                for(Annotation a : as) {
                    if(a instanceof PathParam) {
                        PathParam p = (PathParam)a;
                        match = match.replace("{" + p.value() + "}", ".*");
                    }
                }
            }
            Pattern pattern = Pattern.compile(Matcher.quoteReplacement(match));
            if(pattern.matcher(request).matches()) {
                return ent.getValue().b;
            }
        }
        return new AnnotationLiteral<PermitAll>() {};
    }
    
    @Override
    public void init(FilterConfig config) throws ServletException{
        try {
            String className = config.getInitParameter("servlet.class.name");
            Class klass = Class.forName(className);

            System.out.println("Servlet Class Name: " + className);
            String rootPath = getPathOrDefault(klass, "/");
            Method mtds[] = klass.getDeclaredMethods();
            for(Method m : mtds) {
                String httpm = HttpMethod.GET;
                String path = rootPath + getPathOrDefault(m, m.getName());
                for(Annotation a : m.getDeclaredAnnotations()) {
                    if(a.annotationType().isAnnotationPresent(HttpMethod.class)) {
                        httpm = a.annotationType().getAnnotation(HttpMethod.class).value();
                    }
                }
                System.out.println();
                Annotation anns[] = m.getDeclaredAnnotations();
                for(Annotation a : anns) {
                    if(a instanceof RolesAllowed || a instanceof PermitAll || a instanceof DenyAll) {
                        matcher.put(httpm + " " + path, new Pair(m, a));
                        System.out.println("Method: " + httpm + " " + path + ", Permissions: " + a.toString());
                        break;
                    }
                }
            }
       } catch (ClassNotFoundException ex) {
           Logger.getLogger(Jsr250SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   @Override
   public void doFilter(ServletRequest request, 
                 ServletResponse response,
                 FilterChain chain) 
                 throws java.io.IOException, ServletException {
       
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String url = req.getPathInfo();
            String method = req.getMethod();
            System.out.println(method + " " + url);
            
            final String auth = req.getHeader( "Authorization" );
            Annotation secure = getPermissionsForPath(method + " " + url);
            System.out.println(secure.annotationType().toString());
            if ( secure.annotationType().equals(PermitAll.class) ) {
                System.out.println("PERMITS ALL USERS");
                chain.doFilter(request, response);
                return;
            }
            if ( auth != null ) {
                final int index = auth.indexOf( ' ' );
                if ( index > 0 ) {
                    try {
                        final String[] credentials = StringUtils.split( new String( Base64.decode( auth.substring( index ) ), Charsets.UTF_8 ), ':' );
                        
                        if ( credentials.length == 2 && _user.equals( credentials[0] ) && _password.equals( credentials[1] ) ) {
                            chain.doFilter( req, res );
                            return;
                        }
                    } catch (Base64DecodingException ex) {
                        Logger.getLogger(Jsr250SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            res.setHeader( "WWW-Authenticate", "Basic realm=\"" + _realm + "\"" );
            res.sendError( HttpServletResponse.SC_UNAUTHORIZED );
        }
   }
   
   @Override
   public void destroy( ){
      /* Called before the Filter instance is removed 
      from service by the web container*/
   }
   
}