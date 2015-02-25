/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import java.util.Arrays;
import javax.inject.Inject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The MicroBlogUserService is a simple implementation of the
 * UserDetailsService. It uses the MicroBlogService to check
 * for the existence of a user and return the credentials when
 * looked up by username.
 * 
 * @author Brian Becker
 */
public class MicroBlogUserService implements UserDetailsService {
    
    @Inject
    private MicroBlogService blogService;    
    
    /**
     * Load the Spring Security UserDetails by the username. These details
     * consist of the user id and the password which were created when the
     * user signed up by posting to the /user resource.
     * 
     * @param   username    user id to retrieve user details for
     * @return  the user details (username, password, credentials)
     * @throws  UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData data = blogService.getUser(username);
        if(data == null) throw new UsernameNotFoundException("User by the requested name was not found.");
        return new User(data.id, data.password.get(), Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_USER") }) );
    }    
    
}
