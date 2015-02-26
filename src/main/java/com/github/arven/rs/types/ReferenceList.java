package com.github.arven.rs.types;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ReferenceList is a key based reference to a list of objects. It does not
 * use full objects, only single elements which provide a link to the objects
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

    @XmlElement private List<String> id;
    
    public ReferenceList() {}
    
    /**
     * Create a new DataReference with the given list of string identifiers.
     * This list should be some kind of unique reference to a series of
     * objects, that can be retrieved from a data store when more information
     * is required for them.
     * 
     * @param   id      The string identifier to use for the reference
     */
    public ReferenceList(List<String> id) { this.id = id; }
    
}