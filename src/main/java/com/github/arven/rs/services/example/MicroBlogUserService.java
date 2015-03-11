/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.services.example;

import com.github.arven.rs.auth.HashedUserInfo;
import com.github.arven.rs.auth.UserService;
import java.util.Arrays;
import javax.inject.Inject;

/**
 * The MicroBlogUserService is a simple implementation of the
 * UserDetailsService. It uses the MicroBlogService to check
 * for the existence of a user and return the credentials when
 * looked up by username.
 * 
 * @author Brian Becker
 */
public class MicroBlogUserService implements UserService {
    
    @Inject
    private MicroBlogService blogService;    
    
    /**
     * Load the Spring Security UserDetails by the username. These details
     * consist of the user id and the password which were created when the
     * user signed up by posting to the /user resource.
     * 
     * @param   username    user id to retrieve user details for
     * @return  the user details (username, password, credentials)
     */
    @Override
    public HashedUserInfo loadUserByUsername(String username) {
        UserData data = blogService.getUser(username);
        if(data == null) throw new RuntimeException("User by the requested name was not found.");
        return new HashedUserInfo("SHA-256", data.getId(), data.getPassword(), Arrays.asList(new String[] {"REST_USER"}));
    }    
    
}