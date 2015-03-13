/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.services.example;

import java.util.Random;
import org.apache.activemq.security.SecurityContext;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class MicroBlogServiceTest {

    @BeforeClass
    public void initialize() {
    }
    
    @DataProvider(name = "userData")
    public Object[][] createUserData() {
        return new Object[][] { 
            {"trfields", "tom", "tom@muckdragon.info", "snowing"}, 
            {"hp27", "humphrey", "hp27@muckdragon.info", "listing"}, 
            {"katherine", "kate", "katherine@muckdragon.info", "correcthorsebatterystaple"} 
        };
    }
    
    @DataProvider(name = "messageData")
    public Object[][] createMessageData() {
        Random r = new Random();
        return new Object[][] {
            {"trfields", "snowing", "Message" + r.nextLong(), "This is yet another test message from #MicroBlogServiceTest."}
        };
    }
    
    @Test(dataProvider = "userData", groups = {"created"})
    public void testCanCreateUser(String id, String nick, String email, String pass) {
        MicroBlogRestService svc = JAXRSClientFactory.create("http://localhost:8080/example", MicroBlogRestService.class);
        svc.addUser(new UserData(id, nick, email, pass));
    }
    
    @Test(dataProvider = "messageData")
    public void testCanPostMessage(String user, String pass, String id, String message) {
        MicroBlogRestService svc = JAXRSClientFactory.create("http://localhost:8080/example", MicroBlogRestService.class, user, pass, null);        
        svc.postMessage(new MessageData(id, message), null);
    }
    
}
