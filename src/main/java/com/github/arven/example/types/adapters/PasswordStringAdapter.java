package com.github.arven.example.types.adapters;

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
        return "[REDACTED]";
    }

    @Override
    public String unmarshal(String string) throws Exception {
        return string;
    }

}
