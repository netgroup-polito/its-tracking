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
import it.polito.dp2.rest.rns.jaxb.DangerousMaterialType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;

@Path("dangerousmaterials")
@Api(value = "/dangerousmaterials")
public class DangerousMaterialResource {

	@GET
    @ApiOperation(
			value = "getDangerousMaterials",
			notes = "allow to retrieve all information about all the dangerous materials currently in the system"
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
	public Response getDangerousMaterials() {
		return Response.status(Status.OK).entity(RNSCore.getInstance().getDangerousMaterials()).build();
	}
	
	@GET
	@Path("/{id}")
    @ApiOperation(
			value = "getDangerousMaterials",
			notes = "allow to retrieve all information about all the dangerous materials currently in the system"
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
	public Response getDangerousMaterial(@PathParam("id") String id, @Context HttpHeaders headers) {
		DangerousMaterialType material = RNSCore.getInstance().getDangerousMaterial(id);
		JAXBElement<DangerousMaterialType> materialJaxb = (new ObjectFactory()).createDangerousMaterial(material);
		
		return (headers.getAcceptableMediaTypes().get(0).toString().equals(MediaType.APPLICATION_XML.toString()))
				? Response.status(Status.OK).entity(materialJaxb).build()
				: Response.status(Status.OK).entity(material).build();
	}
}
