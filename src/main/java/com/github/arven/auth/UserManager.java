/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchResult;

/**
 *
 * @author brian.becker
 */
public class UserManager {
    
    public static final Properties PROPERTIES;
    public static final String USER_CONTEXT;
    public static final String GROUP_CONTEXT;
    
    static {
        PROPERTIES = new Properties();
        try {
            PROPERTIES.load(UserManager.class.getResourceAsStream("/jndi.properties"));
        } catch (IOException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        USER_CONTEXT = PROPERTIES.getProperty("com.github.arven.auth.users");
        GROUP_CONTEXT = PROPERTIES.getProperty("com.github.arven.auth.groups");
    }
    
    public static void create(String id, String name, String last, String pass, Collection<String> roles) {
        try {
            InitialDirContext context = new InitialDirContext( PROPERTIES );
            Attributes attributes = new BasicAttributes();
            attributes.put(new BasicAttribute("objectClass", "inetOrgPerson"));
            attributes.put(new BasicAttribute("uid", id));
            attributes.put(new BasicAttribute("cn", name));
            attributes.put(new BasicAttribute("sn", last));
            attributes.put(new BasicAttribute("userPassword", pass ));
            context.createSubcontext("uid=" + id + "," + USER_CONTEXT, attributes);

            for(String role : roles) {
                ModificationItem[] mods =
                { new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("uniqueMember", "uid=" + id + "," + USER_CONTEXT)) };
                context.modifyAttributes("cn=" + role + "," + GROUP_CONTEXT, mods);
            }
        } catch (NamingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void destroy(String id) {
        try {
            InitialDirContext context = new InitialDirContext( PROPERTIES );
            
            context.destroySubcontext("uid=" + id + "," + USER_CONTEXT);
            
            Attributes matchAttrs = new BasicAttributes(true);
            matchAttrs.put(new BasicAttribute("uniqueMember", "uid=" + id + "," + USER_CONTEXT));
            NamingEnumeration answer = context.search(GROUP_CONTEXT, matchAttrs);
            while(answer.hasMore()) {
                SearchResult sr = (SearchResult)answer.next();
                ModificationItem[] mods =
                { new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", "uid=" + id + "," + USER_CONTEXT)) };
                context.modifyAttributes(sr.getName() + "," + GROUP_CONTEXT, mods);
            }
        } catch (NamingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
