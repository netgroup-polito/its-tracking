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
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;

@Path("roadsegments")
@Api(value = "/roadsegments")
public class RoadSegmentResource {
	@GET
    @Path("/{id}")
    @ApiOperation(
			value = "getRoadSegment",
			notes = "allow to retrieve all information about a specific road segment in the system"
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
    public Response getGate(@PathParam("id") String roadSegmentId, @Context HttpHeaders headers){
    		RoadSegmentReaderType road = RNSCore.getInstance().getRoadSegment(roadSegmentId);
	    	JAXBElement<RoadSegmentReaderType> jaxbPlace = (new ObjectFactory()).createRoadSegment(road);
	    	return 
    			(headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
    			? Response.status(Status.OK).entity(jaxbPlace).build()
    			: Response.status(Status.OK).entity(road).build();
    }
}
