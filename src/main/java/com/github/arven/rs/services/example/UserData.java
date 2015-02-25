package com.github.arven.rs.services.example;

import com.github.arven.rs.types.Password;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The UserData class provides all of the general information about a user,
 * aside from relational data. This consists of a user id, a nickname, an
 * email address, as well as a password.
 * 
 * @author Brian Becker
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class UserData {

    @XmlAttribute
    public String id;
    
    @XmlElement
    public String nickname;
    
    @XmlElement
    public String email;
    
    @XmlElement
    public Password password;
    
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
        this.password = new Password(password);
    }
    
}
