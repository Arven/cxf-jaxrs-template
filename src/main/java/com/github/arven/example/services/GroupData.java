/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.NONE)
public class GroupData {   

    @XmlAttribute
    public String id;
    
    @XmlValue
    public String description;
    
    public GroupData() { }
    
    public GroupData(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
}