package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import webscraber.TagCounter;
import webscraber.Tester;

@Path("scrape")
public class WebScraperResource {

  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Context
  private UriInfo context;

  public WebScraperResource() {
  }
  
  @GET
  @Path("/test")
  public Response test() {
      return Response.ok().entity("Hello World").build();
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tags")
  public Response getTags() throws InterruptedException {
    Tester t = new Tester();
    //List<TagCounter> result = t.getTagCounters();
    return Response.ok().entity(gson.toJson(t.getTagCounters())).build();
  }

  private Response makeResponse() throws InterruptedException {
    Tester t = new Tester();
    //List<TagCounter> result = t.getTagCounters();
    return Response.ok().entity(gson.toJson(t.getTagCounters())).build();
  }

}
