package it.polito.dp2.rest.rns.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("admin")
@Api(value = "/admin")
public class AdminResource {

	@GET
	@Path("/system")
    @ApiOperation(
			value = "getSystem",
			notes = "allow to retrieve all information about all the system"
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
	public Response getSystem() {
		return Response.status(Status.OK).entity(RNSCore.getInstance().getSystemTot()).build();
	}

}
