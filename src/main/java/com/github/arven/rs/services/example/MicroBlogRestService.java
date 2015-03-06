package com.github.arven.rs.services.example;

import com.github.arven.rs.types.DataList;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * MicroBlogRestService is the REST service front end for the MicroBlogService.
 * Most of the calls are delegated directly to the MicroBlogService. There are
 * a number of annotations on each of the paths, which specify the majority of
 * the REST parameters.
 * 
 * @author Brian Becker
 */
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Path("/v1")
@Singleton
@DeclareRoles({"rest-user"})
public class MicroBlogRestService {
    
    public static int MAX_LIST_SPAN = 10;
    
    @Inject
    private MicroBlogService blogService;
    
    @Path("/version") @GET
    @Produces("text/plain")
    public String getVersion() {
        return "v1.0";
    }

    @Path("/user") @POST
    public void addUser(UserData user) {
        blogService.addUser(user);
    }
    
    @Path("/user/{name}") @GET
    public UserData getUser(@PathParam("name") String name) {
        return blogService.getUser(name);
    }
    
    @Path("/secure/user/{name}") @DELETE @RolesAllowed({"rest-user"})
    public void removeUser(@PathParam("name") String name, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.removeUser(name);
        }
    }
    
    @Path("/user/{name}/friends") @GET
    public DataList getFriendsList(@PathParam("name") String name, @MatrixParam("offset") Integer offset) {
        return new DataList(blogService.getFriends(name), offset, MAX_LIST_SPAN, false);
    }
    
    @Path("/secure/user/{name}/friends/{friend}") @PUT @RolesAllowed({"rest-user"})
    public void addFriend(@PathParam("name") String name, @PathParam("friend") String friend, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.addFriend(name, friend);
        }
    }
    
    @Path("/secure/user/{name}/friends/{friend}") @DELETE @RolesAllowed({"rest-user"})
    public void removeFriend(@PathParam("name") String name, @PathParam("friend") String friend, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.removeFriend(name, friend);
        }
    }
    
    @Path("/secure/post") @POST @RolesAllowed({"rest-user"})
    public void postMessage(MessageData post, final @Context SecurityContext ctx) {
        blogService.addPost(ctx.getUserPrincipal().getName(), post);
    }
    
    @Path("/post/{name}") @GET
    public DataList getMessagesByUser(@PathParam("name") String name, @MatrixParam("offset") Integer offset) {
        return new DataList(blogService.getPosts(name), offset, MAX_LIST_SPAN, true);
    }
    
    @Path("/secure/group") @POST @RolesAllowed({"rest-user"})
    public void createGroup(GroupData group, final @Context SecurityContext ctx) {
        blogService.addGroup(group, ctx.getUserPrincipal().getName());
    }
    
    @Path("/group/{name}") @GET
    public GroupData getGroupInfo(@PathParam("name") String name) {
        return blogService.getGroup(name);
    }
    
    @Path("/secure/group/{name}") @DELETE @RolesAllowed({"rest-user"})
    public void leaveOrDisbandGroup(@PathParam("name") String name, final @Context SecurityContext ctx) {
        blogService.leaveGroup(name, ctx.getUserPrincipal().getName());
    }
    
    @Path("/group/{name}/members") @GET
    public DataList getGroupMembers(@PathParam("name") String name, @MatrixParam("offset") Integer offset) {
        return new DataList(blogService.getGroupMembers(name), offset, MAX_LIST_SPAN, false);
    }
    
    @Path("/secure/group/{name}/join") @POST @RolesAllowed({"rest-user"})
    public void joinGroup(@PathParam("name") String name, final @Context SecurityContext ctx) {
        blogService.addGroupMember(name, ctx.getUserPrincipal().getName());
    }
 
}