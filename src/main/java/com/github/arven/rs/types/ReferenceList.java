package com.github.arven.rs.types;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * DataReference is a key based reference to another object. It is not a
 * full object, only a single element which provides a link to the object
 * in a way that can be programmatically determined. This is useful for
 * database queries that return a large list of objects which will rarely
 * be iterated through, or where only the key of a query result actually
 * matters.
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "references")
@XmlAccessorType(XmlAccessType.NONE)
public class ReferenceList {

    @XmlElement public List<String> id;
    
    public ReferenceList() {}
    
    /**
     * Create a new DataReference with the given string identifier. This
     * reference will be serialized using the real type name and a single
     * attribute which labels it as a reference.
     * 
     * @param   id      The string identifier to use for the reference
     */
    public ReferenceList(List<String> id) { this.id = id; }
    
}