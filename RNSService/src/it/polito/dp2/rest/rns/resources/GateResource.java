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

@Path("gates")
@Api(value = "/gates")
public class GateResource {

	@GET
    @ApiOperation(
			value = "getGate",
			notes = "allow to retrieve all information about all gates stored in the system"
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
    public Response getGates() {
		return Response.status(Status.OK).entity("gates").build();
    }
    
    @GET
    @Path("/{id}")
    @ApiOperation(
			value = "getGate",
			notes = "allow to retrieve all information about a specific gate in the system"
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
    public Response getGate(@PathParam("id") int gateId){
    	return null;
    }
    
    @POST
    @Path("/{id}")
    @ApiOperation(
			value = "enterSystem",
			notes = "allow to ask for permission to enter the system"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = "Created"),
					@ApiResponse(code = 400, message = "Bad Request"),
					@ApiResponse(code = 401, message = "Unauthorized"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
    @Consumes({
    	MediaType.APPLICATION_XML,
    	MediaType.APPLICATION_JSON
    })
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.TEXT_XML
	})
    public Response enterSystem(@PathParam("id") int gateId) {
    	return null;
    }
    
    @DELETE
    @Path("/{id}")
    @ApiOperation(
			value = "exitSystem",
			notes = "allow a vehicle to notify it's exiting the system"
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
    public Response exitSystem(@PathParam("id") int gateId) {
    	return null;
    }
}
