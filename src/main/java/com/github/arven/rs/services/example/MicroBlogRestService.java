package com.github.arven.rs.services.example;

import com.github.arven.rs.types.DataList;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
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
@Stateless
@DeclareRoles({"User", "Anonymous"})
public class MicroBlogRestService {
    
    public static int MAX_LIST_SPAN = 10;
    
    @Resource
    private EJBContext ectx;
    
    @Inject
    private MicroBlogService blogService;
    
    /**
     * The most trivial example of a REST method, simply get the version and
     * return it as a raw string with a MIME type of text/plain.
     * 
     * @return  Version of this demo
     */
    @Path("/version") @GET
    @Produces("text/plain")
    public String getVersion() {
        return "v1.0";
    }
    
    /**
     * Get User Information for debugging the authentication system, mainly
     * to determine if a user is in the appropriate roles for accessing the
     * system.
     * 
     * @param   ctx   User's security context
     * @return  User security information
     */
    @Path("/info") @GET
    @Produces("text/plain")
    public String getInfo(final @Context SecurityContext ctx) {
        return ectx.getCallerPrincipal().toString()+":"+ectx.getCallerPrincipal().getName()+"=(user:"+ectx.isCallerInRole("user")+")";
    }

    /**
     * This method adds a user via the injected MicroBlogService backend,
     * which adds the user and credential to the LDAP server as well as adds
     * the user itself to the database.
     * 
     * @param user 
     */
    @Path("/user") @POST
    public void addUser(UserData user) {
        blogService.addUser(user);
    }
    
    /**
     * This method gets a user and displays it as one of the primary content
     * types.
     * 
     * @param name
     * @return 
     */
    @Path("/user/{name}") @GET
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
    @Path("/user/{name}") @DELETE @RolesAllowed({"User"})
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
    @Path("/user/{name}/friends") @GET
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
     * @param name
     * @param friend
     * @param ctx 
     */
    @Path("/user/{name}/friends/{friend}") @PUT @RolesAllowed({"User"})
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
    @Path("/user/{name}/friends/{friend}") @DELETE @RolesAllowed({"User"})
    public void removeFriend(@PathParam("name") String name, @PathParam("friend") String friend, final @Context SecurityContext ctx) {
        if(ctx.getUserPrincipal().getName().equals(name)) {
            blogService.removeFriend(name, friend);
        }
    }
    
    /**
     * For a given user, this method posts a message in their name. Any
     * user can call this method with any parameters, the name is recalled
     * directly from the security context.
     * 
     * @param post
     * @param ctx 
     */
    @Path("/post") @POST @RolesAllowed({"User"})
    public void postMessage(MessageData post, final @Context SecurityContext ctx) {
        blogService.addPost(ctx.getUserPrincipal().getName(), post);
    }
    
    /**
     * For a given user, this method gets all the messages posted in their
     * name. Any user can call this method with any username as a parameter.
     * The parameters allow setting the offset where the list is shown, as
     * the entire list will not be returned if it is too large.
     * 
     * @param name
     * @param offset
     * @return 
     */
    @Path("/post/{name}") @GET
    public DataList getMessagesByUser(@PathParam("name") String name, @MatrixParam("offset") Integer offset) {
        return new DataList(blogService.getPosts(name), offset, MAX_LIST_SPAN, true);
    }
    
    /**
     * For a given user, this method creates a group and inserts the user into
     * the group. Any user can call this method and any group name can be used
     * as long as the group name is available.
     * 
     * @param group
     * @param ctx 
     */
    @Path("/group") @POST @RolesAllowed({"User"})
    public void createGroup(GroupData group, final @Context SecurityContext ctx) {
        blogService.addGroup(group, ctx.getUserPrincipal().getName());
    }
    
    /**
     * For a given group, this method gets the information and returns it back
     * to the user. Any group can be viewed by any user.
     * 
     * @param name
     * @return 
     */
    @Path("/group/{name}") @GET
    public GroupData getGroupInfo(@PathParam("name") String name) {
        return blogService.getGroup(name);
    }
    
    /**
     * For the authenticated user, this method leaves the chosen group. If the
     * group is empty upon leaving, the group will be disbanded as there are
     * no members, and it will be available for any other user to create.
     * 
     * @param name
     * @param ctx 
     */
    @Path("/group/{name}") @DELETE @RolesAllowed({"User"})
    public void leaveOrDisbandGroup(@PathParam("name") String name, final @Context SecurityContext ctx) {
        blogService.leaveGroup(name, ctx.getUserPrincipal().getName());
    }
    
    /**
     * For the given group name, get the list of members. This can be called
     * by any user regardless of group membership.
     * 
     * @param name
     * @param offset
     * @return 
     */
    @Path("/group/{name}/members") @GET
    public DataList getGroupMembers(@PathParam("name") String name, @MatrixParam("offset") Integer offset) {
        return new DataList(blogService.getGroupMembers(name), offset, MAX_LIST_SPAN, false);
    }
    
    /**
     * For the given group name, join the group. This can be called by any user
     * which is not currently a member of the group, and they will then be
     * shown in the members list.
     * 
     * @param name
     * @param ctx 
     */
    @Path("/group/{name}/join") @POST @RolesAllowed({"User"})
    public void joinGroup(@PathParam("name") String name, final @Context SecurityContext ctx) {
        blogService.addGroupMember(name, ctx.getUserPrincipal().getName());
    }
 
}