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

@XmlRootElement(name = "ref")
@XmlAccessorType(XmlAccessType.NONE)
public class DataReference {
    @XmlAttribute   public String id;
    public DataReference() {}
    public DataReference(String id) { this.id = id; }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof DataReference) {
            return this.hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}