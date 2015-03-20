/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rs.services.example;

import static com.github.arven.rs.services.example.MicroBlogRestResource.MAX_LIST_SPAN;

import com.github.arven.rs.types.DataList;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author brian.becker
 */
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Stateless
@Path("/v1/user/{name}")
public class UserRestResource {
        
    @Inject
    private MicroBlogService blogService;
    
    /**
     * This method gets a user and displays it as one of the primary content
     * types.
     * 
     * @param name
     * @return 
     */
    @GET
    public UserData getUser(@PathParam("name") String name) {
        return blogService.getUser(name);
    }
    
    /**
     * This method deletes a user and returns nothing to the user which calls
     * this function. Only the user himself can delete the user, there is no
     * concept of administrator users in this demo.
     * 
     * @param name
     * @param ctx 
     */
    @DELETE @RolesAllowed({"User"})
    public void removeUser(@PathParam("name") String name, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.removeUser(name);
        }
    }
    
    /**
     * This method gets a list of friends for a given user, all users are
     * allowed to look at this information.
     * 
     * @param name
     * @param offset
     * @return 
     */
    @Path("/friends") @GET
    public DataList getFriendsList(@PathParam("name") String name, @MatrixParam("offset") Integer offset) {
        return new DataList(blogService.getFriends(name), offset, MAX_LIST_SPAN, false);
    }
    
    /**
     * For a given user, this method adds a friend. Only the user which is
     * authenticated can call this method for themselves, but it can be
     * called with a parameter representing any person in the database
     * as long as they exist.
     * 
     * There is no transacted friends list support which requires a request
     * to a user in this demo.
     * 
     * @param friend
     * @param ctx 
     */
    @Path("/friends/{friend}") @PUT @RolesAllowed({"User"})
    public void addFriend(@PathParam("name") String name, @PathParam("friend") String friend, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.addFriend(name, friend);
        }
    }
    
    /**
     * For a given user, this method removes a friend. Only the user which
     * is authenticated can call this method for themselves.
     * 
     * The friends list is not mutual, therefore it has no effect on the
     * other user in this demo.
     * 
     * @param name
     * @param friend
     * @param ctx 
     */
    @Path("/friends/{friend}") @DELETE @RolesAllowed({"User"})
    public void removeFriend(@PathParam("name") String name, @PathParam("friend") String friend, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.removeFriend(name, friend);
        }
    }
    
}
