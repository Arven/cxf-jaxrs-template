package com.github.arven.rs.services.example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;

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
public class MessageData {
    
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
        String[] temp = StringUtils.substringsBetween(this.message.replaceAll("[" + Pattern.quote("!\"$%&'()*+,-./:;<=>?@[\\]^_`{|}~") + "]", " ").concat(" "), "#", " ");
        if (temp != null) {
            for(String tag : temp) {
                tags.add(StringUtils.lowerCase(tag));
            }
        }
        if(tags.isEmpty()) { return null; }
        return tags;
    }
    
}