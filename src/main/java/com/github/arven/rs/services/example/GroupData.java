package com.github.arven.rs.services.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * The GroupData class is a simple class which provides a description to
 * a given group. This description does not contain a list of members,
 * just the group name and description.
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.NONE)
public class GroupData {

    @XmlID @XmlAttribute
    private String id;
    
    @XmlValue
    private String description;
    
    public GroupData() { }
    
    /**
     * Create a new GroupData with the given id and description.
     * 
     * @param   id              Name of the group which will be used in API
     * @param   description     Description of the group
     */
    public GroupData(String id, String description) {
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
    
}