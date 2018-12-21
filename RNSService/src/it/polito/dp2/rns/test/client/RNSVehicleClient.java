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
	public String createVehicle(VehicleReaderType vehicle) {
		// Instantiate the new client
		Client client = ClientBuilder.newClient();
		ObjectFactory factory = new ObjectFactory(); // To create Jaxb-generated objects
		String vehicleId = "";
		
		Response response = 
				client.target(serverUrl + "/vehicles")
					.request()
					.post(Entity.xml(factory.createVehicle(vehicle)));
		
		// Check the response
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
			
			case 201:
				System.out.println(TAG + "Vehicle created!");
				vehicleId = response.readEntity(String.class);
				break;
				
			default:
				break;
				
		}
		
		// Close the client connection and release resources
		client.close();
		
		return vehicleId;
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
				client.target(serverUrl + "/vehicles/" + id)
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
				vehicle = response.readEntity(VehicleReaderType.class);
				break;
				
			default:
				break;	
		}

		return vehicle;
	}
}
