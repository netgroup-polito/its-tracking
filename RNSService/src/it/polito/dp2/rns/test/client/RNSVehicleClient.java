package it.polito.dp2.rns.test.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import it.polito.dp2.rest.rns.jaxb.*;

public class RNSVehicleClient {
	private static final String serverUrl = "http://localhost:8080/rns/webapi";
	private static final String TAG = "[RNSClient] ";
	
	public RNSVehicleClient() { }
	

	/**
	 * Function to create a new vehicle on the server.
	 * @param vehicle = the vehicle to be created
	 * @return string that is the id of the vehicle just created
	 */
	public Places createVehicle(VehicleReaderType vehicle) {
		// Instantiate the new client
		Client client = ClientBuilder.newClient();
		ObjectFactory factory = new ObjectFactory();
		Places path = factory.createPlaces();
		
		Response response = 
				client.target(serverUrl + "/vehicles")
					.request()
					.post(Entity.xml(factory.createVehicle(vehicle)));
		
		// Check the response
		switch(response.getStatusInfo().getStatusCode()){
			case 500:
				System.err.println(TAG + "Internal server error!");
				return null;
			
			case 404:
				System.err.println(TAG + "Not found!");
				return null;
			
			case 400:
				System.err.println(TAG + "Bad request!");
				return null;
			
			case 201:
				System.out.println(TAG + "Vehicle created!");
				path = response.readEntity(Places.class);
				break;
				
			default:
				break;
				
		}
		
		// Close the client connection and release resources
		client.close();
		
		return path;
	}
	
	/**
	 * Function to retrieve from server a specific vehicle.
	 * @param id = the id of the vehicle to be retrieved
	 * @return the requested vehicle if it exists, otherwise null
	 */
	public VehicleReaderType getVehicle(String id) {
		VehicleReaderType vehicle = null;
		Client client = ClientBuilder.newClient();
		
		Response response = 
				client
				.target(serverUrl + "/vehicles/" + id)
				.request()
				.header("Accept", "application/xml")
				.get();
		
		switch(response.getStatusInfo().getStatusCode()){
			case 500:
				System.err.println(TAG + "Internal server error!");
				break;
			
			case 404:
				System.err.println(TAG + "Not found!");
				break;
			
			case 400:
				System.err.println(TAG + "Bad request!");
				break;
			
			case 200:
				System.out.println(TAG + "Vehicle retrieved!");
				vehicle = response.readEntity(VehicleReaderType.class);
				break;
				
			default:
				break;	
		}

		return (vehicle == null) ? null : vehicle;
	}

	/**
	 * Function to retrieve from the service all the gates
	 * in the system
	 * @return an object containing all gates in the system
	 */
	public Gates getGates() {
		Gates gates = null;
		Client client = ClientBuilder.newClient();
		
		Response response = 
				client.target(serverUrl + "/gates")
				.request()
				.get();
		
		switch(response.getStatusInfo().getStatusCode()){
			case 500:
				System.err.println(TAG + "Internal server error!");
				break;
			
			case 404:
				System.err.println(TAG + "Not found!");
				break;
			
			case 400:
				System.err.println(TAG + "Bad request!");
				break;
			
			case 200:
				System.out.println(TAG + "Vehicle retrieved!");
				gates = response.readEntity(Gates.class);
				break;
				
			default:
				break;	
		}

		return gates;
	}
}
