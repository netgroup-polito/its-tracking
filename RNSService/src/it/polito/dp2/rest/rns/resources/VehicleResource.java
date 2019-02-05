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
import javax.xml.bind.JAXBElement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import io.swagger.annotations.ApiResponse;

@Path("vehicles")
@Api(value = "/vehicles")
public class VehicleResource {
	private final RNSCore instance = RNSCore.getInstance();
	
	@GET
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
		MediaType.APPLICATION_JSON
	})
	public Response getVehicles() {
		return Response.status(Status.OK).entity("vehicles").build();
	}
	
	@GET
    @Path("/{id}")
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
			MediaType.APPLICATION_JSON
	})
    public Response getVehicle(@PathParam("id") String vehicleId) {
		VehicleReaderType vehicle = this.instance.getVehicle(vehicleId);
    	JAXBElement<VehicleReaderType> jaxbVehicle = (new ObjectFactory()).createVehicle(vehicle);
    	return Response.status(Status.OK).entity(jaxbVehicle).build();
    }
    
    @GET
    @Path("/{id}/path")
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
			MediaType.APPLICATION_JSON
	})
    public Response getVehiclePath(@PathParam("id") int vehicleId) {
    	return null;
    }
    
    @POST
    @ApiOperation(
			value = "createVehicle",
			notes = "allow to create a vehicle in the system"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = "Created"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON
	})
    @Consumes({
    	MediaType.APPLICATION_XML,
    	MediaType.APPLICATION_JSON
    })
    public Response createVehicle(JAXBElement<VehicleReaderType> vehicle){
    	String vehicleId = this.instance.addVehicle(vehicle.getValue());
		return Response.status(Status.CREATED).entity(vehicleId).build();
    }
    
    @PUT
    @Path("/{id}")
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
			MediaType.APPLICATION_JSON
	})
    @Consumes({
    	MediaType.APPLICATION_XML,
    	MediaType.APPLICATION_JSON
    })
    public Response updateVehicle(@PathParam("id") int vehicleId) {
    	return null;
    }
    
    @DELETE
    @Path("/{id}")
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
			MediaType.APPLICATION_JSON
	})
    public Response deleteVehicle() {
    	return null;
    }
}