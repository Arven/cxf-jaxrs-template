/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import com.google.common.collect.Lists;
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
    @XmlAttribute   public Integer limit;
    @XmlAttribute   public Integer size;
    @XmlAttribute   public Boolean reverse;
    public DataList() {}
    public DataList(List list, Integer offset, Integer span, Boolean reverse) {
        this.offset = (offset == null || offset == 0) ? null : offset;
        offset = offset == null ? 0 : offset;
        
        if(list == null) {
            list = Collections.EMPTY_LIST;
        }
        
        if(reverse) {
            list = Lists.reverse(list);
        }
        
        this.size = list.size();
        this.limit = offset + span < size ? span : null;
        if(offset <= list.size()) {
            this.entry = list.subList(offset, Math.min(offset + span, list.size()));
        } else {
            this.entry = Collections.EMPTY_LIST;
        }
    }
}