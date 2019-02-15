package it.polito.dp2.rest.rns.resources;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.rest.rns.exceptions.InvalidEntryPlaceException;
import it.polito.dp2.rest.rns.exceptions.PlaceFullException;
import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;
import it.polito.dp2.rest.rns.exceptions.VehicleAlreadyInSystemException;
import it.polito.dp2.rest.rns.exceptions.VehicleNotInSystemException;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
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
		return Response.status(Status.OK).entity(this.instance.getVehicles()).build();
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
    public Response getVehicle(@PathParam("id") String vehicleId, @Context HttpHeaders headers) {
		try {
			VehicleReaderType vehicle = this.instance.getVehicle(vehicleId);
	    		JAXBElement<VehicleReaderType> jaxbVehicle = (new ObjectFactory()).createVehicle(vehicle);
	    		return
    				(headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
    				? Response.status(Status.OK).entity(jaxbVehicle).build()
    				: Response.status(Status.OK).entity(vehicle).build();
		} catch(VehicleNotInSystemException vnise) {
			return Response.status(Status.BAD_REQUEST).entity("Vehicle not present in the system").build();
		}
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
    		// TODO: get vehicle path
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
    		System.out.println("++++++++++++++++++++++++++++++++++++++");
    		System.out.println((new Date()).toString());
    		System.out.println("CREATE VEHICLE " + vehicle.getValue().getId() + " " + vehicle.getValue().getState());
		try {
			String vehicleId = this.instance.addVehicle(vehicle.getValue());
			return Response.status(Status.CREATED).entity(vehicleId).build();
		} catch (PlaceFullException e) { // PLACE FULL
			System.out.println(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (VehicleAlreadyInSystemException e) { // ALREADY ADDED VEHICLE
			System.out.println(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (InvalidEntryPlaceException e) { // GATE NON VALID
			System.out.println(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (UnsatisfiableException e) {
			System.out.println(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
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
    public Response updateVehicle(@PathParam("id") String vehicleId, JAXBElement<VehicleReaderType> vehicle) {
    		System.out.println("++++++++++++++++++++++++++++++++++++++");
    		System.out.println((new Date()).toString());
    		System.out.println("UPDATE VEHICLE " + vehicleId + " --- " + vehicle.getValue().getPosition());
    		try {
			this.instance.updateVehicle(vehicle.getValue());
			return Response.status(Status.OK).entity(vehicleId).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
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
    public Response deleteVehicle(@PathParam("id") int vehicleId) {
    		try {
    			this.instance.deleteVehicle(String.valueOf(vehicleId));
    			return Response.status(Status.OK).entity("deleted").build();
    		} catch (Exception e) {
    			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    		}
    }
}
