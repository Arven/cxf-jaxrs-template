package com.github.arven.rs.services.example;

import java.util.List;

import com.github.arven.rs.types.PasswordStringAdapter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="users")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class UserData {

	@Basic @Column(name="id", unique=true)
    @XmlID @XmlAttribute
    private String id;
    
	@Basic @Column(name="nick")
    @XmlElement
    private String nickname;
    
	@Basic @Column(name="email")
    @XmlElement
    private String email;
    
	@Basic @Column(name="password")
    @XmlElement
    @XmlJavaTypeAdapter(PasswordStringAdapter.class)
    private String password;
	
	@OneToMany @Column(name="message")
	private List<MessageData> messages;
    
    public UserData() { }
    
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
    
}
