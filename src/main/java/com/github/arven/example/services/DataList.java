/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "list")
@XmlAccessorType(XmlAccessType.NONE)
public class DataList {
    @XmlAnyElement  public Collection<Object> entry;
    @XmlAttribute   public Integer offset;
    @XmlAttribute   public Integer span;
    @XmlAttribute   public Integer size;
    public DataList() {}
    public DataList(List list, Integer offset, Integer span) {
        offset = offset == null ? 0 : offset;
        this.offset = offset;
        this.span = span;
        if(list == null) {
            list = Collections.EMPTY_LIST;
        }
        
        this.size = list.size();
        if(offset > list.size()) {
            this.entry = Collections.EMPTY_LIST;
        } else {
            this.entry = list.subList(offset, Math.min(offset + span, list.size()));
        }
    }
}