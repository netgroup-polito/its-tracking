package it.polito.dp2.rest.rns.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

@Path("rns")
@Api(value = "/rns")
public class RNSResource {
	private final RNSCore instance = RNSCore.getInstance();
	
	/**
	 * In the constructor we want to retrieve the instance of RNSCore in order to have
	 * access to all methods of the intelligence of the system, in order to store, 
	 * update or retrieve data.
	 */
	public RNSResource() { }
	
	@SuppressWarnings("unlikely-arg-type")
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
		RnsReaderType rns = this.instance.getSystem();
		JAXBElement<RnsReaderType> rnsJaxb = (new ObjectFactory()).createRns(rns);
		return 
			(headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
			? 	Response.status(Status.OK).entity(rnsJaxb).build() :
				Response.status(Status.OK).entity(rns).build();
	}
}
