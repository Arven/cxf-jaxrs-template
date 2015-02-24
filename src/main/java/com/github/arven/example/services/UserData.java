/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple User POJO for the MicroBlog server. 
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class UserData {

    @XmlAttribute
    public String id;
    
    @XmlElement
    public String nickname;
    
    @XmlElement
    public String email;
    
    @XmlElement
    public String password;
    
    public UserData() { }
    
    public UserData(String id, String nickname, String email, String password) {
        this.id       = id;
        this.nickname = nickname;
        this.email    = email;
        this.password = password;
    }
    
}
