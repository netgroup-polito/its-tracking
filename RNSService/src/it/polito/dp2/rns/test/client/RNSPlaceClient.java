package it.polito.dp2.rns.test.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import it.polito.dp2.rest.rns.jaxb.*;

public class RNSPlaceClient {
	private static final String serverUrl = "http://localhost:8080";
	private static final String TAG = "[RNSClient] ";
	
	public RNSPlaceClient() { }
	
	/**
	 * Function to create a new Complex Place on the server.
	 * @param place = the place to be created
	 * @return string that is the id of the place just created
	 */
	public String createComplexPlace(ComplexPlaceReaderType place) {
		// Instantiate the new client
		Client client = ClientBuilder.newClient();
		ObjectFactory factory = new ObjectFactory(); // To create Jaxb-generated objects
		String placeId = "";
		
		Response response = 
				client.target(serverUrl + "/places/complexPlaces")
					.request()
					.post(Entity.xml(factory.createComplexPlace(place)));
		
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
				System.out.println(TAG + "Place created!");
				placeId = response.readEntity(String.class);
				break;
				
			default:
				break;
				
		}
		
		// Close the client connection and release resources
		client.close();
		
		return placeId;
	}
	
	/**
	 * Function to create a new Simple Place on the server.
	 * @param place = the place to be created
	 * @return string that is the id of the place just created
	 */
	public String createSimplePlace(SimplePlaceReaderType place) {
		// Instantiate the new client
		Client client = ClientBuilder.newClient();
		ObjectFactory factory = new ObjectFactory(); // To create Jaxb-generated objects
		String placeId = "";
		
		Response response = 
				client.target(serverUrl + "/places/simplePlaces")
					.request()
					.post(Entity.xml(factory.createPlace(place)));
		
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
				System.out.println(TAG + "Place created!");
				placeId = response.readEntity(String.class);
				break;
				
			default:
				break;
				
		}
		
		// Close the client connection and release resources
		client.close();
		
		return placeId;
	}
	
	/**
	 * Function to retrieve from server a specific complex place.
	 * @param id = the id of the place to be retrieved
	 * @return the requested complex place if it exists, otherwise null
	 */
	public ComplexPlaceReaderType getComplexPlace(String id) {
		ComplexPlaceReaderType place = null;
		Client client = ClientBuilder.newClient();
		
		Response response = 
				client.target(serverUrl + "/" + id)
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
			
			case 201:
				System.out.println(TAG + "Place retrieved!");
				place = response.readEntity(ComplexPlaceReaderType.class);
				break;
				
			default:
				break;	
		}

		return place;
	}
}
