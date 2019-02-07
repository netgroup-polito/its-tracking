package it.polito.dp2.rest.rns.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to translate from id of object given from the client
 * to neo4j id used server side
 * @author eugeniogallea
 */
public class IdTranslator {
	private Map<String, String> id2neo4j = new HashMap<>();
	private static IdTranslator instance = null;
	
	private IdTranslator() { }
	
	public static IdTranslator getInstance() {
		if(instance == null) {
			instance = new IdTranslator();
		}
		
		return instance;
	}
	
	/**
	 * Function to add a new connection between an id and the 
	 * corresponding one in neo4j
	 * @param id = the id client side
	 * @param neo4jId = the id neo4j has given the object
	 */
	public void addIdTranslation(String id, String neo4jId) {
		this.id2neo4j.put(id, neo4jId);
	}
	
	/**
	 * Function to retrieve the neo4j id, given the client
	 * one
	 * @param id = the id client has for an objects
	 * @return the corresponding neo4j id
	 */
	public String getIdTranslation(String id) {
		return this.id2neo4j.get(id);
	}

	/**
	 * Function to remove a correspondence from the map
	 * @param vehicleId = id of the vehicle of which the correspondence
	 * has to be removed
	 */
	public void removeTranslation(String vehicleId) {
		this.id2neo4j.remove(vehicleId);
	}
}
