/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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
