package it.polito.RNS.service;

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

@Path("")
@Api(value = "/")
public class RNSResource {
	@Context
	private RNSCore instance;
	
	/**
	 * In the constructor we want to retrieve the instance of RNSCore in order to have
	 * access to all methods of the intelligence of the system, in order to store, 
	 * update or retrieve data.
	 */
	public RNSResource() {
		this.instance = RNSCore.getInstance();
	}
	
	@GET
	@Path("/")
	@ApiOperation(
			value = "helloWorld",
			notes = "allow to check if the Web Service has been correctly deployed"
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
	public Response helloWorld(){
		return Response.status(Status.OK).entity("Hello world!").build();
	}
	
	//********************************************//
	//											  //
	//				GET SECTION					  //
	//											  //
	//********************************************//
	@GET
	@Path("/rns")
	@ApiOperation(
			value = "getSystemState",
			notes = "allow to retrieve all information of the system"
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
	public Response getSystemState() {
		return Response.status(Status.OK).entity("rns").build();
	}
	
    @GET
    @Path("/gates")
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
			MediaType.TEXT_XML
	})
    public Response getGates() {
    	return null;
    }
    
    @GET
    @Path("/gates/{id}")
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
    
    @GET
    @Path("/places")
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
			MediaType.TEXT_XML
	})
    public Response getPlaces() {
    	return null;
    }
    
    @GET
    @Path("/places/{id}")
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
    
    @GET
    @Path("/vehicles")
    @ApiOperation(
			value = "getVehicles",
			notes = "allow to retrieve all information about all the vehicles currently in the system"
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
    public Response getVehicles() {
    	return null;
    }
    
    @GET
    @Path("/vehicles/{id}")
    @ApiOperation(
			value = "getVehicle",
			notes = "allow to retrieve all information about a specific vehicle in the system"
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
    public Response getVehicle(@PathParam("id") int vehicleId) {
    	return null;
    }
    
    @GET
    @Path("/vehicles/{id}/path")
    @ApiOperation(
			value = "getVehiclePath",
			notes = "allow to retrieve all information about the path a specific vehicle has to follow"
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
    public Response getVehiclePath(@PathParam("id") int vehicleId) {
    	return null;
    }
    
    //********************************************//
  	//											  //
  	//				PUT SECTION					  //
  	//											  //
  	//********************************************//
    @PUT
    @Path("/places/{id}")
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
    
    @PUT
    @Path("/vehicles/{id}")
    @ApiOperation(
			value = "updateVehicle",
			notes = "allow to update information about a specific vehicle"
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
    public Response updateVehicle(@PathParam("id") int vehicleId) {
    	return null;
    }
    
    //********************************************//
  	//											  //
  	//				POST SECTION				  //
  	//											  //
  	//********************************************//
    @POST
    @Path("/gates/{id}")
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
    
    
    //********************************************//
  	//											  //
  	//				DELETE SECTION				  //
  	//											  //
  	//********************************************//
    @DELETE
    @Path("/gates/{id}")
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
    
    @DELETE
    @Path("/vehicles/{id}")
    @ApiOperation(
			value = "deleteVehicle",
			notes = "allow an administrator to delete a specific vehicle"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 401, message = "Unauthorized"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.TEXT_XML
	})
    public Response deleteVehicle() {
    	return null;
    }
}