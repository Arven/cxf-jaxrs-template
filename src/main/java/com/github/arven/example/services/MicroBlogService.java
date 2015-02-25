/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.ClientErrorException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response.Status;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Singleton
@Named
public class MicroBlogService implements UserDetailsService {
    
    private final Map<String, UserData> users;
    private final ListMultimap<String, MessageData> posts;
    private final ListMultimap<String, DataReference> friends;
    private final Map<String, GroupData> groups;
    private final ListMultimap<String, DataReference> members;
    
    public MicroBlogService() {
        users = new HashMap<String, UserData>();
        posts = ArrayListMultimap.create();
        groups = new HashMap<String, GroupData>();
        members = ArrayListMultimap.create();
        friends = ArrayListMultimap.create();
    }
    
    public UserData getUser( String user ) {
        if(users.containsKey(user)) {
            return users.get(user);            
        } else {
            throw new NotFoundException();
        }
    }
    
    public void addUser( UserData user ) {
        if(!users.containsKey(user.id)) {
            users.put(user.id, user);
        } else {
            throw new ClientErrorException(Status.CONFLICT);
        }
    }    
    
    public List<MessageData> getPosts( String user ) {
        if(posts.containsKey(user)) {
            return posts.get(user);
        } else {
            throw new NotFoundException();
        }
    }    

    public void addPost( String user, MessageData post ) {
        post.date = (Calendar.getInstance().getTime());
        posts.put(user, post);
    }
    
    public GroupData getGroup( String group ) {
        return groups.get(group);
    }
    
    public List<DataReference> getGroupMembers( String group ) {
        return members.get(group);
    }
    
    public void addGroup( GroupData group, String username ) {
        if(!groups.containsKey(group.id)) {
            groups.put(group.id, group);
            addGroupMember(group.id, username);
        } else {
            throw new ClientErrorException(Status.CONFLICT);
        }
    }
    
    public void addGroupMember( String group, String username ) {
        members.put(group, new DataReference(username));
    }
    
    public void leaveGroup( String group, String username ) {
        if(members.containsKey(group)) {
            members.get(group).remove(new DataReference(username));
            if(members.get(group).isEmpty()) {
                removeGroup(group);
            }
        }
    }
    
    public void removeGroup( String group ) {
        groups.remove(group);
    }
    
    public List<DataReference> getFriends( String username ) {
        if(friends.containsKey(username)) {
            return friends.get(username);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    public void addFriend( String username, String friendname ) {
        removeFriend(username, friendname);
        if(users.containsKey(friendname)) {
            friends.put(username, new DataReference(friendname));
        }
    }
    
    public void removeFriend( String username, String friendname ) {
        if(friends.containsKey(username) && users.containsKey(friendname)) {
            friends.get(username).remove(new DataReference(friendname));
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData data = getUser(username);
        return new User(data.id, data.password.get(), Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_USER") }) );
    }    
    
}
