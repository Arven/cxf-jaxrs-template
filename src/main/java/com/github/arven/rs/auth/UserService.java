/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

import java.util.Map;

/**
 *
 * @author Brian Becker
 */
public interface UserService {
    
    public abstract HashedUserInfo loadUserByUsername(String username);
    
}
