package it.polito.dp2.rest.rns.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.RnsReaderType;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import sun.java2d.pipe.SpanShapeRenderer.Simple;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

@Path("rns")
@Api(value = "/rns")
public class RNSResource {
	
	/**
	 * In the constructor we want to retrieve the instance of RNSCore in order to have
	 * access to all methods of the intelligence of the system, in order to store, 
	 * update or retrieve data.
	 */
	public RNSResource() { }
	
	@GET
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
			MediaType.APPLICATION_JSON,
			MediaType.TEXT_PLAIN
	})
	public Response getSystemState(@Context HttpHeaders headers) {
		RnsReaderType rns = RNSCore.getInstance().getSystem(false);
		JAXBElement<RnsReaderType> rnsJaxb = (new ObjectFactory()).createRns(rns);
		return 
			(headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
			? 	Response.status(Status.OK).entity(rnsJaxb).build() :
				Response.status(Status.OK).entity(rns).build();
	}
	
	@GET
	@Path("/places/{id}")
	@ApiOperation(
			value = "getPlace",
			notes = "allow to retrieve all information of a place"
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
	public Response getPlace(@PathParam("id") String placeId, @Context HttpHeaders headers) {
		
		SimplePlaceReaderType place = Neo4jInteractions.getInstance().getPlace(placeId);
		JAXBElement<SimplePlaceReaderType> placeJaxb = (new ObjectFactory()).createPlace(place);
		
		return 
			(headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
			? 	Response.status(Status.OK).entity(placeJaxb).build() :
				Response.status(Status.OK).entity(place).build();
	}
}
