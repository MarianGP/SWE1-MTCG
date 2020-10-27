package server.model;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/messages")
public class PathHandler {
    HttpRequest request;

    @GET
    public void getMessages() {
        request = new HttpRequest(method);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessages() {
        return "Messages";
    }

    @Path("/1")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getOneMessage() {
        return "Messages";
    }

    @Path("/2")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void addNewMessage() {
        "Messages";
    }


}
