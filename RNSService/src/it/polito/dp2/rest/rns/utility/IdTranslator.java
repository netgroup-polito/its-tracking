package it.polito.dp2.rest.rns.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is used to translate from id of object given from the client
 * to neo4j id used server side
 * @author eugeniogallea
 */
public class IdTranslator {
	private Map<String, String> id2neo4j = new HashMap<>();
	private Map<String, String> neo4j2id = new HashMap<>();
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
		this.neo4j2id.put(neo4jId, id);
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
		String toBeRemovedKey = "";
		for(Entry<String, String> entry : this.neo4j2id.entrySet()) {
			if(entry.getValue().equals(vehicleId)) {
				toBeRemovedKey = entry.getKey();
				break;
			}
		}
		
		if(!toBeRemovedKey.equals("")) this.neo4j2id.remove(toBeRemovedKey);
	}

	/**
	 * Function to retrieve from neo4j id the client id
	 * @param neo4jId = id in neo4j
	 * @return the id client side
	 */
	public String fromNeo4jId(String neo4jId) {
		return this.neo4j2id.get(neo4jId);
	}
}
