/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Singleton
@Named
public class MicroBlogService implements UserDetailsService {
    
    public Map<String, UserData> users;
    public MultivaluedMap<String, MessageData> posts;
    public MultivaluedMap<String, DataReference> friends;
    public Map<String, GroupData> groups;
    public MultivaluedMap<String, DataReference> members;
    
    public MicroBlogService() {
        users = new HashMap<String, UserData>();
        posts = new MultivaluedHashMap<String, MessageData>();
        groups = new HashMap<String, GroupData>();
        members = new MultivaluedHashMap<String, DataReference>();
        friends = new MultivaluedHashMap<String, DataReference>();
    }
    
    public UserData getUser( String user ) {
        if(!users.containsKey(user))
            throw new NotFoundException();
        return users.get(user);
    }
    
    public void addUser( UserData user ) {
        users.put(user.id, user);
    }    
    
    public List<MessageData> getPosts( String user ) {
        if(!posts.containsKey(user))
            throw new NotFoundException();
        return posts.get(user);
    }    

    public void addPost( String user, MessageData post ) {
        post.date = (Calendar.getInstance().getTime());
        posts.add(user, post);
    }
    
    public GroupData getGroup( String group ) {
        return groups.get(group);
    }
    
    public List<DataReference> getGroupMembers( String group ) {
        return members.get(group);
    }
    
    public void addGroup( GroupData group ) {
        groups.put(group.id, group);
    }
    
    public void addGroupMember( String group, String username ) {
        members.add(group, new DataReference(username));
    }
    
    public List<DataReference> getFriends( String username ) {
        if(friends.containsKey(username)) {
            return friends.get(username);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    public void addFriend( String username, String friendname ) {
        friends.add(username, new DataReference(friendname));
    }
    
    public void removeFriend( String username, String friendname ) {
        if(friends.containsKey(username)) {
            friends.get(username).remove(new DataReference(friendname));
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData data = getUser(username);
        return new User(data.id, data.password.get(), Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_USER") }) );
    }    
    
}
