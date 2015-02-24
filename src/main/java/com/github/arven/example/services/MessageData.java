/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import org.apache.commons.lang.StringUtils;

/**
 * Message Data Type
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageData {
   
    
    @XmlRootElement(name = "messages")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class MessageDataList<T> {
        
        @XmlElement
        public List<T> list;
        
        @XmlAttribute
        public int getSize() {
            return list.size();
        }
        
        public MessageDataList() {}
        
        public MessageDataList(List<T> list) {
            this.list = list;
        }
        
    }
    
    @XmlAttribute
    public String id;
    
    @XmlAttribute
    public Date date;
    
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
    
    public void setDate(Date date) {
        this.date = date;
    }
    
}