package com.github.arven.rs.services.example;

import javax.ejb.Stateless;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

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
@Path("v1")
@Stateless
public class MicroBlogRestResource {
    
    public static int MAX_LIST_SPAN = 10;
    
    @Inject
    private MicroBlogService blogService;
    
    /**
     * The most trivial example of a REST method, simply get the version and
     * return it as a raw string with a MIME type of text/plain.
     * 
     * @return  Version of this demo
     */
    @Path("version") @GET
    @Produces("text/plain")
    public String getVersion() {
        return "v1.0";
    }
    
    @Path("group/{group}")
    public GroupRestResource getGroupSubResource() {
        return new GroupRestResource(blogService);
    }
    
    @Path("user/{name}")
    public UserRestResource getUserSubResource() {
        return new UserRestResource(blogService);
    }
 
}