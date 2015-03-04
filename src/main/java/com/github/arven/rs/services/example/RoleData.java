/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.services.example;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * The RoleData class is a simple class which provides a description to
 * a given role. These roles are for permissions in the REST service.
 * 
 * @author Brian Becker
 */
@Entity
@Table(name="ROLEDATA")
@XmlRootElement(name = "role-group")
@XmlAccessorType(XmlAccessType.NONE)
public class RoleData {

    @Id
    @XmlID @XmlAttribute
    private String id;
    
    @Basic
    @XmlValue
    private String description;
	
    @ManyToMany
    private List<UserData> members;
    
    public RoleData() {
    	this.members = new LinkedList<UserData>();
    }
    
    /**
     * Create a new GroupData with the given id and description.
     * 
     * @param   id              Name of the group which will be used in API
     * @param   description     Description of the group
     */
    public RoleData(String id, String description) {
    	super();
        this.id = id;
        this.description = description;
    }
    
    /**
     * Get the group id
     * 
     * @return	the group id
     */
    public String getId() {
    	return this.id;
    }
    
    /**
     * Get the list of members
     * @return 
     */
    public List<UserData> getMembers() {
    	return this.members;
    }
    
    /**
     * Set the list of members
     * @param members
     */
    public void setMembers(List<UserData> members) {
    	this.members = members;
    }    
    
}