/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.util.AnnotationLiteral;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

@Secured @Interceptor
public class Jsr250AuthorizationInterceptor {
    
     public static Annotation permissions(AnnotatedElement ae) {
        for(Annotation a : ae.getDeclaredAnnotations()) {
            if(a instanceof RolesAllowed || a instanceof PermitAll || a instanceof DenyAll) {
                return a;
            }
        }
        return new AnnotationLiteral<PermitAll>() {};
    }
    
    public static boolean allows(Annotation a, SecurityContext ctx) {
        if(a.annotationType().equals(RolesAllowed.class)) {
            RolesAllowed ra = (RolesAllowed) a;
            for(String role : ra.value()) {
                if(ctx.isUserInRole(role)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static SecurityContext security(InvocationContext params) {
        for(Object o : params.getParameters()) {
            if(o instanceof SecurityContext) {
                return (SecurityContext)o;
            }
        }
        return null;
    }
    
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        SecurityContext security = security(ctx);
        Annotation inner = permissions(ctx.getMethod());
        Annotation outer = permissions(ctx.getMethod().getDeclaringClass());
        if(outer.annotationType().equals(PermitAll.class) || allows(outer, security)) {
            if(inner.annotationType().equals(PermitAll.class) || allows(inner, security)) {
                return ctx.proceed();
            }
        }
        throw new WebApplicationException(401);
    }
   
}