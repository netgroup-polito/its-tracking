package it.polito.dp2.rest.rns.resources;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import it.polito.dp2.rest.rns.utility.Constants;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("rns")
@Api(value = "/rns")
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
			MediaType.TEXT_PLAIN
	})
	public Response getSystemState() {
		return Response.status(Status.OK).entity(instance.neo4j.helloWorld()).build();
	}
}
