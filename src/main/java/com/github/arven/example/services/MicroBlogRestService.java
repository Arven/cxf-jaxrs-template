/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.example.services;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

public class MicroBlogRestService {
     
    @Inject
    private MicroBlogService blogService;
    
    @Path("/version") @GET @Produces({ MediaType.TEXT_PLAIN })
    public String version() {
        return "v1.0";
    }

    @Path("/user") @POST @Consumes({ MediaType.APPLICATION_XML })
    public void addUser(UserData user) {
        blogService.addUser(user);
    }
    
    @Path("/user/{name}") @GET @Produces({ MediaType.APPLICATION_XML })
    public UserData getUser(@PathParam("name") String name) {
        return blogService.getUser(name);
    }
    
    @Path("/post") @POST @RolesAllowed({"ROLE_USER"}) @Consumes({ MediaType.APPLICATION_XML })
    public void postMessage(MessageData post, final @Context SecurityContext ctx) {
        blogService.addPost(ctx.getUserPrincipal().getName(), post);
    }
    
    @Path("/post/{name}") @GET @Produces({ MediaType.APPLICATION_XML })
    public DataList getMessagesByUser(@PathParam("name") String name) {
        return new DataList(blogService.getPosts(name));
    }    
    
    @Path("/group") @POST @RolesAllowed({"ROLE_USER"}) @Consumes({ MediaType.APPLICATION_XML })
    public void createGroup(GroupData group, final @Context SecurityContext ctx) {
        blogService.addGroup(group);
        blogService.addGroupMember(group.id, ctx.getUserPrincipal().getName());
    }
    
    @Path("/group/{name}") @GET @Produces({ MediaType.APPLICATION_XML })
    public GroupData getGroupInfo(@PathParam("name") String name) {
        return blogService.getGroup(name);
    }
    
    @Path("/group/{name}/members") @GET @Produces({ MediaType.APPLICATION_XML })
    public DataList getGroupMembers(@PathParam("name") String name) {
        return new DataList(blogService.getGroupMembers(name));
    }
    
    @Path("/group/{name}/join") @POST @RolesAllowed({"ROLE_USER"})
    public void joinGroup(@PathParam("name") String name, final @Context SecurityContext ctx) {
        blogService.addGroupMember(name, ctx.getUserPrincipal().getName());
    }
 
}