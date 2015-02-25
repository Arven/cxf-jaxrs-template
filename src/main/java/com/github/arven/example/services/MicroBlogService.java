package com.github.arven.example.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ClientErrorException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * MicroBlogService is a backend implementation, with no database or any
 * persistent store, which can be used to provide services via the class
 * MicroBlogRestService. This service provides just enough functionality
 * to demonstrate and test the web service layer itself.
 * 
 * @author Brian Becker
 */
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
    
    /**
     * Get the user data for a given user.
     * 
     * @param   user        user id for the user
     * @return  The data for the user
     */
    public UserData getUser( String user ) {
        return users.get(user);
    }
    
    /**
     * Create a new user by providing a UserData object for configuration. It
     * must contain a user id, in order to refer to the user.
     * 
     * @param   user        user data for the user, containing user id
     */
    public void addUser( UserData user ) {
        if(!users.containsKey(user.id)) {
            users.put(user.id, user);
        } else {
            throw new ClientErrorException(Status.CONFLICT);
        }
    }    
    
    /**
     * Get a list of posts from the given user. If the user does not exist,
     * then we will get back an empty list containing no posts. If the user
     * has not posted anything, we will also get back an empty list of no
     * posts.
     * 
     * @param   user        user id for the user whose posts we want
     * @return  a list of posts from the user
     */
    public List<MessageData> getPosts( String user ) {
        return posts.get(user);
    }    

    /**
     * Add a post for the given user. If the user does not exist, the post
     * will still be added under the chosen user id. Therefore, it is
     * necessary to check whether the user exists in the user list before
     * posting a message.
     * 
     * @param   user        user id for the user who will be posting
     * @param   post        message which should be posted by the user
     */
    public void addPost( String user, MessageData post ) {
        posts.put(user, post);
    }
    
    /**
     * Get the group information for a given group. If the group does not
     * exist then we will get a null value.
     * 
     * @param   group       group id for the group we want information about
     * @return  The group information
     */
    public GroupData getGroup( String group ) {
        return groups.get(group);
    }
    
    /**
     * Get a list of group members for a given group. If the group does not
     * exist then we will get an empty list.
     * 
     * @param   group       group id for the group we want members of
     * @return  The group member list
     */
    public List<DataReference> getGroupMembers( String group ) {
        return members.get(group);
    }
    
    /**
     * Add a group which does not exist. If the group does already exist,
     * then we will return an HTTP 409 Conflict exception. The user who
     * creates the group will be put in the group, but the group is not
     * contingent on the user who created the group staying in the group.
     * 
     * @param   group       group data for the group we want to create
     * @param   username    username of the person who is creating the group
     */
    public void addGroup( GroupData group, String username ) {
        if(!groups.containsKey(group.id)) {
            groups.put(group.id, group);
            addGroupMember(group.id, username);
        } else {
            throw new ClientErrorException(Status.CONFLICT);
        }
    }
    
    /**
     * Add a group member to a group which does exist. The group must exist,
     * or the method will do nothing, but the username is expected to exist.
     * 
     * @param   group       group data for the group we want to join
     * @param   username    username who is joining the group
     */
    public void addGroupMember( String group, String username ) {
        if(groups.containsKey(group)) {
            members.put(group, new DataReference(username));
        }
    }
    
    /**
     * Leave a group which a given user is a member of. The group need not
     * exist, if it does the method will simply do nothing. If the group
     * exists, and this user is the last user in a given group, then we will
     * simply remove the entire group data. This allows groups to be
     * reclaimed.
     * 
     * @param   group       group id which user is trying to leave
     * @param   username    user id which is leaving the group
     */
    public void leaveGroup( String group, String username ) {
        members.remove(group, new DataReference(username));
        if(members.get(group).isEmpty()) {
            this.removeGroup(group);
        }
    }
    
    /**
     * Remove a group without any questions asked. If there are members in
     * the group then the entire group becomes empty and it is reclaimed
     * for creation.
     * 
     * @param   group       group id for removal
     */
    public void removeGroup( String group ) {
        groups.remove(group);
    }
    
    /**
     * Get the friend list for a given user. If the user has not added any
     * friends yet, or the user does not exist, then the friends list will
     * be empty.
     * 
     * @param   username    user id for friends list
     * @return  the friends list, or empty if not valid
     */
    public List<DataReference> getFriends( String username ) {
        return friends.get(username);
    }
    
    /**
     * Add a friend to the friend list for a given user. If the user has
     * added the friend already, it will be removed and replaced at the
     * bottom of the list. If the friend user id does not exist, then no
     * friend will be added.
     * 
     * @param   username    user id which is adding a friend
     * @param   friendname  user id which is being added as a friend
     */
    public void addFriend( String username, String friendname ) {
        this.removeFriend(username, friendname);
        if(users.containsKey(friendname)) {
            friends.put(username, new DataReference(friendname));
        }
    }
    
    /**
     * Remove a friend from a given user's friend list. If the user has
     * not added the user to their friend list, then the remove operation
     * will remove nothing.
     * 
     * @param   username    user id which is removing a friend
     * @param   friendname  user id which is being removed as a friend
     */
    public void removeFriend( String username, String friendname ) {
        friends.remove(username, new DataReference(friendname));
    }
    
    /**
     * Load the Spring Security UserDetails by the username. These details
     * consist of the user id and the password which were created when the
     * user signed up by posting to the /user resource.
     * 
     * @param   username    user id to retrieve user details for
     * @return  the user details (username, password, credentials)
     * @throws  UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData data = this.getUser(username);
        if(data == null) throw new UsernameNotFoundException("User by the requested name was not found.");
        return new User(data.id, data.password.get(), Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_USER") }) );
    }    
    
}
