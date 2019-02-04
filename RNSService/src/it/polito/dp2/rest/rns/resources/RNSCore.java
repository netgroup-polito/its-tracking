package it.polito.dp2.rest.rns.resources;

import java.util.HashMap;
import java.util.Map;

import it.polito.dp2.rest.rns.jaxb.ComplexPlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import it.polito.dp2.rest.rns.utility.Constants;
import it.polito.dp2.rest.rns.utility.MapLoader;

/**
 * This class is the core of the application. All the end-points should refer to some
 * function in here to perform tasks. Here resides all the intelligence of the service.
 * 
 * It has been define a singleton pattern in order to ensure having only one instance and
 * keep a status here in the server. This is measure that has to be taken since for each
 * incoming request a new RNSResource object is allocated. All the RNSResource objects must
 * refer to the same instance of RNSCore in order to keep track of the status of the system.
 * 
 * @author dp2
 *
 */
public class RNSCore {
	private static RNSCore instance = null; // Instance of the class
	private Neo4jInteractions neo4j;
	private Map<String, ComplexPlaceReaderType> complexPlaces;
	private Map<String, SimplePlaceReaderType> simplePlaces;
	private Map<String, VehicleReaderType> vehicles;
	
	/**
	 * Private constructor, so that the instance of the object can only be accessed
	 * via static method getInstance()
	 */
	private RNSCore(){
		this.neo4j = Neo4jInteractions.getInstance();
		this.complexPlaces = new HashMap<>();
		this.simplePlaces = new HashMap<>();
		this.vehicles = new HashMap<>();
		
		MapLoader.loadMap();
	}
	
	/**
	 * This method allow external objects to obtain the instance of the class in order
	 * to access methods or obtain information about the status of the system.
	 * 
	 * @return RNSCore instance
	 */
	public static RNSCore getInstance(){
		if(instance == null){
			instance = new RNSCore();
		}
		
		return instance;
	}

	public String helloWorld() {
		return this.neo4j.helloWorld();
	}

	/**
	 * Function to add a complex place as a node, both in the local map
	 * database and in Neo4j
	 * @param value = the place to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addPlace(ComplexPlaceReaderType value) {
		// TODO: establish relationships
		String id = this.neo4j.createNode(value);
		value.setId(id);
		System.out.print("****** Added complex place: " + id + "******");
		this.complexPlaces.put(id, value);
		return id;
	}
	
	/**
	 * Function to add a simple place as a node, both in the local map
	 * database and in Neo4j
	 * @param value = the place to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addPlace(SimplePlaceReaderType value) {
		// TODO: establish relationships
		String id = this.neo4j.createNode(value);
		value.setId(id);
		System.out.print("****** Added simple place: " + id + "******");
		this.simplePlaces.put(id, value);
		return id;
	}
	
	/**
	 * Function to get a complex place
	 * @param id
	 * @return the complex place
	 */
	public ComplexPlaceReaderType getComplexPlace(String id) {
		return (this.complexPlaces.containsKey(id)) ? this.complexPlaces.get(id) : null;
	}
	
	/**
	 * Function to get a simple place
	 * @param id
	 * @return the simple place
	 */
	public SimplePlaceReaderType getSimplePlace(String id) {
		return (this.simplePlaces.containsKey(id)) ? this.simplePlaces.get(id) : null;
	}
	
	/**
	 * Function to add a vehicle as a node, both in the local map
	 * database and in Neo4j
	 * @param value = the vehicle to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addVehicle(VehicleReaderType value) {
		// TODO: establish relationships
		String id = this.neo4j.createNode(value);
		value.setId(id);
		System.out.print("****** Added vehicle: " + id + "******");
		this.vehicles.put(id, value);
		return id;
	}
	
	/**
	 * Function to get a vehicle
	 * @param id
	 * @return the vehicle
	 */
	public VehicleReaderType getVehicle(String id) {
		return (this.vehicles.containsKey(id)) ? this.vehicles.get(id) : null;
	}

	/**
	 * Function to add a gate as a node in Neo4j
	 * @param value = the gate to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addGate(GateReaderType value) {
		// TODO: establish relationships
		String id = this.neo4j.createNode(value);
		value.setId(id);
		System.out.print("****** Added gate: " + id + "******");
		return id;
	}
	
	
	public GateReaderType getGate(String gateId) {
		return this.neo4j.getGate(gateId);
	}
}
