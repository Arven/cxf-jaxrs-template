package com.github.arven.rs.types;

import com.google.common.io.BaseEncoding;
import java.security.MessageDigest;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This Password adapter is used to marshal a String type in a Password
 * object and prevent the password from being read out via JAXB. A real
 * implementation would likely have a separate data transfer object for
 * the authentication logic as well as the user information logic.
 * 
 * @author Brian Becker
 */
public class PasswordStringAdapter extends XmlAdapter<String, String> {

    @Override
    public String marshal(String string) throws Exception {
        return string;
    }

    @Override
    public String unmarshal(String string) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return BaseEncoding.base64().encode(md.digest(string.getBytes("UTF-8")));
    }

}
