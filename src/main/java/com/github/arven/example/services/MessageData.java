/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import org.apache.commons.lang.StringUtils;

/**
 * Message Data Type
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "message")
public class MessageData {
    
    @XmlAttribute
    public String id;
    
    @XmlValue
    public String message;
    
    public MessageData () { }
    
    public MessageData (String id, String message) {
        this.id = id;
        this.message = message;
    }
    
    @XmlAttribute
    public List<String> getTags() throws Exception {
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