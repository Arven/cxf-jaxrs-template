/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.services.example;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author brian.becker
 */
@ApplicationPath("/v1")
@Stateless
public class MicroBlogApplication extends Application {
        
    @Override
    public java.util.Set<java.lang.Class<?>> getClasses() {
        Set<Class<?>> s = Sets.newHashSet();
        s.add(MicroBlogRestResource.class);
        s.add(UserRestResource.class);
        s.add(GroupRestResource.class);
        return s;
    }
    
}
