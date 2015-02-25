package com.github.arven.rs.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

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
@XmlRootElement(name = "ref")
@XmlAccessorType(XmlAccessType.NONE)
public class DataReference<DataType> {

    @XmlAttribute   public String id;
    
    public DataReference() {}
    
    /**
     * Create a new DataReference with the given string identifier. This
     * reference will be serialized using the real type name and a single
     * attribute which labels it as a reference.
     * 
     * @param   id      The string identifier to use for the reference
     */
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