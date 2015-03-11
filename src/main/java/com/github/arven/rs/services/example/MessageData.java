package com.github.arven.rs.services.example;

import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The MessageData class provides some basic information about a message
 * posted by a user, or some other type of simple text message.
 * 
 * @author Brian Becker
 */
@Entity
@Table(name="MESSAGEDATA")
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageData implements Serializable {
        
    @Id
    @XmlID @XmlAttribute
    private String id;
    
    @Basic
    @XmlAttribute
    private Date date;
    
    @Basic
    @XmlValue
    private String message;
    
    public MessageData () {
        this.date = (Calendar.getInstance().getTime());
    }
    
    /**
     * Create a new MessageData with the given ID and message. The data such
     * as the tags used will be automatically generated upon request. The
     * date will be set based on the current time when this object is created.
     * 
     * @param   id          The id or title of the message
     * @param   message     The message text, created as a value
     */
    public MessageData (String id, String message) {
        super();
        this.id = id;
        this.message = message;
    }
    
    /**
     * Get the list of tags from the message by parsing the string with a
     * regular expression. The tags are in the format #tagname throughout
     * the message, and they should all be picked up by the parser. This
     * cannot be set on a message, and is only available as an attribute
     * to be read by JAXB or a direct call.
     * 
     * @return  List of tags in a message, generated on demand
     */
    @XmlAttribute
    public List<String> getTags() {
        List<String> tags = new ArrayList<String>();
        Iterable<String> it = Iterables.filter(Splitter.on(" ").omitEmptyStrings().split(message), Predicates.containsPattern("^#.*$"));
        tags.addAll(Arrays.asList(Iterables.toArray(it, String.class)));
        if(tags.isEmpty()) { return null; }
        return tags;
    }
    
}