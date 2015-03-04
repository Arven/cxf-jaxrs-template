package com.github.arven.rs.services.example;

import java.util.LinkedList;
import java.util.List;

import com.github.arven.rs.types.PasswordStringAdapter;
import com.google.common.io.BaseEncoding;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The UserData class provides all of the general information about a user,
 * aside from relational data. This consists of a user id, a nickname, an
 * email address, as well as a password.
 * 
 * @author Brian Becker
 */
@Entity
@Table(name="USERDATA")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class UserData implements Serializable {

    @Id
    @XmlID @XmlAttribute
    private String id;
    
    @Basic
    @XmlElement
    private String nickname;
    
    @Basic
    @XmlElement
    private String email;
    
    @Basic
    @XmlElement
    @XmlJavaTypeAdapter(PasswordStringAdapter.class)
    private String password;
	
    @OneToMany @JoinColumn(name="USERDATA_ID")
    private List<MessageData> messages;
	
    @ManyToMany(mappedBy = "members")
    private List<GroupData> groups;
	
    @OneToMany @JoinTable(name="HAS_FRIEND")
    private List<UserData> friends;
    
    @ManyToMany(mappedBy = "members")
    private List<RoleData> roles;
    
    public UserData() {
    	this.messages = new LinkedList<MessageData>();
    	this.groups = new LinkedList<GroupData>();
    	this.friends = new LinkedList<UserData>();
        this.roles = new LinkedList<RoleData>();
    }
    
    /**
     * Create a new UserData with the user id, nickname, email, and password
     * for the user.
     * 
     * NOTE: The password is stored internally in a Password object, which
     * prevents the marshaling of the actual password to client requests,
     * regardless of permissions. Either no value at all, or a placeholder
     * value may be returned.
     * 
     * @param   id          User id for the user
     * @param   nickname    Nickname for the user
     * @param   email       Email address for the user
     * @param   password    Password for the user (used for authentication)
     */
    public UserData(String id, String nickname, String email, String password) {
    	super();
        this.id       = id;
        this.nickname = nickname;
        this.email    = email;
        this.password = password;
    }
    
    /**
     * Get the user id
     * 
     * @return	the user's id
     */
    public String getId() {
    	return this.id;
    }
    
    /**
     * Get the user password
     * 
     * @return the user's password
     */
    public String getPassword() {
    	return this.password;
    }
    
    /**
     * Get the list of messages for this user
     */
    public List<MessageData> getMessages() {
    	return this.messages;
    }
    
    /**
     * Get the groups this user is in
     */
    public List<GroupData> getGroups() {
    	return this.groups;
    }
    
    /**
     * Set the groups this user is in
     */
    public void setGroups(List<GroupData> groups) {
    	this.groups = groups;
    }
    
    /**
     * Get the friends this user has
     */
    public List<UserData> getFriends() {
    	return this.friends;
    }
    
    /**
     * Set the friends this user has
     */
    public void setFriends(List<UserData> friends) {
    	this.friends = friends;
    }
    
    /**
     * Get the roles that this user has
     */
    public List<RoleData> getRoles() {
        return this.roles;
    }
    
    /**
     * Set the roles that this user has
     */
    public void setRoles(List<RoleData> roles) {
        this.roles = roles;
    }
    
}
