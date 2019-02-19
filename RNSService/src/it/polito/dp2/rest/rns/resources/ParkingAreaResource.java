package it.polito.dp2.rest.rns.resources;

import javax.ws.rs.GET;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.ParkingAreas;

@Path("parkingareas")
@Api(value = "/parkingareas")
public class ParkingAreaResource {

	@GET
    @Path("")
    @ApiOperation(
			value = "getParkingAreas",
			notes = "allow to retrieve all information about a specific parking area in the system"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON,
			MediaType.TEXT_XML
	})
	public Response getParkingAreas() {
		ParkingAreas parks = RNSCore.getInstance().getParkingAreas();
		
		return Response.status(Status.OK).entity(parks).build();
	}
	
	@GET
    @Path("/{id}")
    @ApiOperation(
			value = "getParkingArea",
			notes = "allow to retrieve all information about a specific parking area in the system"
	)
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 500, message = "Internal Server Error")
			}
	)
	@Produces({
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON,
			MediaType.TEXT_XML
	})
    public Response getParkingArea(@PathParam("id") String parkId, @Context HttpHeaders headers){
    		ParkingAreaReaderType park = RNSCore.getInstance().getParkingArea(parkId);
	    	JAXBElement<ParkingAreaReaderType> jaxbPlace = (new ObjectFactory()).createParkingArea(park);
	    	return 
    			(headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
    			? Response.status(Status.OK).entity(jaxbPlace).build()
    			: Response.status(Status.OK).entity(park).build();
    }
	
}
