/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author tfields
 */
@XmlType
public class Password {
    
    @XmlJavaTypeAdapter(PasswordStringAdapter.class)
    @XmlValue public String password;
    
    public String get() { return this.password; }
    
    public Password() { }
    public Password(String password) { this.password = password; }
    
}
