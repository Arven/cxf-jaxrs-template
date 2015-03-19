/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.auth;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

/**
 *
 * @author brian.becker
 */
public class UserManager {
    
    public static void create(String id, String name, String last, String pass) {
        try {
            Properties properties = new Properties();
            properties.put( Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory" );
            properties.put( Context.PROVIDER_URL, "ldap://localhost:10389" );
            properties.put( Context.REFERRAL, "ignore" );
            InitialDirContext context = new InitialDirContext( properties );
            
            Attributes attributes = new BasicAttributes();
            attributes.put(new BasicAttribute("objectClass", "inetOrgPerson"));
            attributes.put(new BasicAttribute("uid", id));
            attributes.put(new BasicAttribute("cn", name));
            attributes.put(new BasicAttribute("sn", "User"));
            attributes.put(new BasicAttribute("userPassword", "{SHA256}" + pass ));
            context.createSubcontext("uid=" + id + ",ou=users,dc=arven,dc=github,dc=com", attributes);

            ModificationItem[] mods =
            { new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("uniqueMember", "uid=" + id + ",ou=users,dc=arven,dc=github,dc=com")) };
            context.modifyAttributes("cn=User,ou=groups,dc=arven,dc=github,dc=com", mods);
        } catch (NamingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void destroy(String id) {
        try {
            Properties properties = new Properties();
            properties.put( Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory" );
            properties.put( Context.PROVIDER_URL, "ldap://localhost:10389" );
            properties.put( Context.REFERRAL, "ignore" );
            InitialDirContext context = new InitialDirContext( properties );
            
            context.destroySubcontext("uid=" + id + ",ou=users,dc=arven,dc=github,dc=com");
            
            ModificationItem[] mods =
            { new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("uniqueMember", "uid=" + id + ",ou=users,dc=arven,dc=github,dc=com")) };
            context.modifyAttributes("cn=User,ou=groups,dc=arven,dc=github,dc=com", mods);            
        } catch (NamingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
