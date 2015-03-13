/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.services.example;

import com.github.arven.auth.HashedUserInfo;
import com.github.arven.auth.UserInfo;
import com.github.arven.auth.UserService;
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
     * The UserService is very similar to that of Spring Security. It should
     * take a string representing the username and return a UserInfo data
     * type which will be used by the authentication layer. The JAAS service
     * will look up the UserService via JNDI and rely on it to authenticate
     * users.
     * 
     * @param   username    user id to retrieve user details for
     * @return  the user details (username, password, credentials)
     */
    @Override
    public UserInfo loadUserByUsername(String username) {
        UserData data = blogService.getUser(username);
        if(data == null) throw new RuntimeException("User by the requested name was not found.");
        return new HashedUserInfo("SHA-256", data.getId(), data.getPassword(), Arrays.asList(new String[] {"user"}));
    }    
    
}