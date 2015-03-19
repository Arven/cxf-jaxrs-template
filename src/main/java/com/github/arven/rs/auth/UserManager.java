/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

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
    
    public static final String USERS = "com.github.arven.auth.users";
    public static final String GROUPS = "com.github.arven.auth.groups";
    public static final String DOMAIN = "com.github.arven.auth.domain";
    
    public static void create(String id, String name, String last, String pass, Collection<String> roles) {
        try {
            Properties properties = new Properties();
            properties.load(UserManager.class.getResourceAsStream("/jndi.properties"));
            InitialDirContext context = new InitialDirContext( properties );
            
            Attributes attributes = new BasicAttributes();
            attributes.put(new BasicAttribute("objectClass", "inetOrgPerson"));
            attributes.put(new BasicAttribute("uid", id));
            attributes.put(new BasicAttribute("cn", name));
            attributes.put(new BasicAttribute("sn", last));
            attributes.put(new BasicAttribute("userPassword", pass ));
            context.createSubcontext("uid=" + id + "," + properties.getProperty(USERS) + "," + properties.getProperty(DOMAIN), attributes);

            for(String role : roles) {
                ModificationItem[] mods =
                { new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("uniqueMember", "uid=" + id + "," + properties.getProperty(USERS) + "," + properties.getProperty(DOMAIN))) };
                context.modifyAttributes("cn=" + role + "," + properties.getProperty(GROUPS) + "," + properties.getProperty(DOMAIN), mods);
            }
        } catch (NamingException | IOException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void destroy(String id) {
        try {
            Properties properties = new Properties();
            properties.load(UserManager.class.getResourceAsStream("/jndi.properties"));
            InitialDirContext context = new InitialDirContext( properties );
            
            context.destroySubcontext("uid=" + id + "," + properties.getProperty(USERS) + "," + properties.getProperty(DOMAIN));
            
            Attributes matchAttrs = new BasicAttributes(true);
            matchAttrs.put(new BasicAttribute("uniqueMember", "uid=" + id + "," + properties.getProperty(USERS) + "," + properties.getProperty(DOMAIN)));
            NamingEnumeration answer = context.search(properties.getProperty(GROUPS) + "," + properties.getProperty(DOMAIN), matchAttrs);
            while(answer.hasMore()) {
                SearchResult sr = (SearchResult)answer.next();
                ModificationItem[] mods =
                { new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", "uid=" + id + "," + properties.getProperty(USERS) + "," + properties.getProperty(DOMAIN))) };
                context.modifyAttributes(sr.getName() + "," + properties.getProperty(GROUPS) + "," + properties.getProperty(DOMAIN), mods);
            }
        } catch (NamingException | IOException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
