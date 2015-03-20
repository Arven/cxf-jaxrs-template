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
     * Get the User Sub Resource Locator
     * 
     * @param name
     * @return 
     */
    @Path("/user/{name}")
    public UserRestResource getUserResource(@PathParam("name") String name) {
        return new UserRestResource(blogService, name);
    }
    
    /**
     * For a given group, this method gets the information and returns it back
     * to the user. Any group can be viewed by any user.
     * 
     * @param name
     * @return 
     */
    @Path("/group/{name}")
    public GroupRestResource getGroupResource(@PathParam("name") String name) {
        return new GroupRestResource(blogService, name);
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
 
}