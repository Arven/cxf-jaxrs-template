/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class HelloWorldService {
     
    final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);
    
    @GET @Path("hello")
    public String sayHello() {
        return "Hello World!!! Local Class!!!";
    }
    
    @RolesAllowed("ROLE_USER") @GET @Path("version")
    public String getVersion(@Context SecurityContext headers) {
        logger.info(headers.getUserPrincipal().toString());
        return "v1.0";
    }
 
}