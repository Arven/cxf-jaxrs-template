package com.github.arven.rs.types;


import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This Password adapter is a simple type which is used to wrap a password
 * and conceal it from users which might be able to receive marshaled data
 * containing a password.
 * 
 * @author Brian Becker
 */
@XmlType
public class Password {
    
    /**
     * The @XmlJavaTypeAdapter specified will prevent the password from
     * being marshaled and will return either a placeholder value or no
     * value whatsoever.
     */
    @XmlJavaTypeAdapter(PasswordStringAdapter.class)
    
    @XmlValue public String password;
    
    /**
     * This allows obtaining the password, but it is not a typical bean
     * property, so it will not be marshaled.
     * 
     * @return  The password which this object represents and masks
     */
    public String get() { return this.password; }
    
    public Password() { }
    
    /**
     * Create a new password with the given string as the password. This
     * object will conceal the password to attempts at getting marshaled
     * data containing the password.
     * 
     * @param   password    The password which is to be wrapped
     */
    public Password(String password) { this.password = password; }
    
}
