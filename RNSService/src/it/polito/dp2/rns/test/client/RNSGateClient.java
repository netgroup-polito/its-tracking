package it.polito.dp2.rns.test.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import it.polito.dp2.rest.rns.jaxb.*;

public class RNSGateClient {
	private static final String serverUrl = "http://localhost:8080/rns/webapi";
	private static final String TAG = "[RNSClient] ";
	
	public RNSGateClient() { }
	

	/**
	 * Function to create a new gate on the server.
	 * @param gate = the gate to be created
	 * @return string that is the id of the gate just created
	 */
	public String createGate(GateReaderType gate) {
		// Instantiate the new client
		Client client = ClientBuilder.newClient();
		ObjectFactory factory = new ObjectFactory(); // To create Jaxb-generated objects
		String gateId = "";
		
		Response response = 
				client.target(serverUrl + "/gates")
					.request()
					.post(Entity.xml(factory.createGate(gate)));
		
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
				gateId = response.readEntity(String.class);
				break;
				
			default:
				break;
				
		}
		
		// Close the client connection and release resources
		client.close();
		
		return gateId;
	}
	
	/**
	 * Function to retrieve from server a specific gate.
	 * @param id = the id of the gate to be retrieved
	 * @return the requested gate if it exists, otherwise null
	 */
	public VehicleReaderType getVehicle(String id) {
		VehicleReaderType gate = null;
		Client client = ClientBuilder.newClient();
		
		Response response = 
				client.target(serverUrl + "/gates/" + id)
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
				gate = response.readEntity(VehicleReaderType.class);
				break;
				
			default:
				break;	
		}

		return gate;
	}
}
