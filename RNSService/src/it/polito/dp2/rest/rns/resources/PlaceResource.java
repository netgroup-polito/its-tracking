package it.polito.dp2.rest.rns.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@Path("places")
@Api(value = "/places")
public class PlaceResource {

	@GET
	@ApiOperation(
			value = "getPlaces",
			notes = "allow to retrieve all information about all the places stored in the system"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
		MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN
	})
	public Response getPlaces() {
		return Response.status(Status.OK).entity("places").build();
	}
    
    @GET
    @Path("/{id}")
    @ApiOperation(
			value = "getPlace",
			notes = "allow to retrieve all information about a specific place stored in the system"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.TEXT_XML
	})
    public Response getPlace(@PathParam("id") int placeId) {
    	return null;
    }
    
    @PUT
    @Path("/{id}")
    @ApiOperation(
			value = "updatePosition",
			notes = "allow to update the position of a specific vehicle"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = "Created"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.TEXT_XML
	})
    @Consumes({
    	MediaType.APPLICATION_XML,
    	MediaType.APPLICATION_JSON
    })
    public Response updatePosition(@PathParam("id") int placeId, @QueryParam("vId") int vehicleId) {
    	return null;
    }
}
