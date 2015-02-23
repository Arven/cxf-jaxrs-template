/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple User POJO for the MicroBlog server. 
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "user")
public class UserData {   

    @XmlAttribute
    public String id;
    
    public String nickname;
    public String email;
    public String password;
    
    public UserData() { }
    
    public UserData(String id, String nickname, String email, String password) {
        this.id       = id;
        this.nickname = nickname;
        this.email    = email;
        this.password = password;
    }
    
    public UserData(String id) {
        this.id       = id;
    }
    
}
