package it.polito.dp2.rest.rns.resources;

import java.util.List;
import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.Gates;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RnsReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.jaxb.Vehicles;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import it.polito.dp2.rest.rns.utility.IdTranslator;
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
	private IdTranslator id2neo4j;
	
	/**
	 * Private constructor, so that the instance of the object can only be accessed
	 * via static method getInstance()
	 */
	private RNSCore(){
		this.neo4j = Neo4jInteractions.getInstance();
		this.id2neo4j = IdTranslator.getInstance();
		
		// Load the map of the system
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

	/**
	 * Hello world function. Just for debug purposes.
	 * @return string of a newly created hello world node in neo4j
	 */
	public String helloWorld() {
		return this.neo4j.helloWorld();
	}
	
	/**
	 * Function to add a vehicle as a node, both in the local map
	 * database and in Neo4j
	 * @param value = the vehicle to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addVehicle(VehicleReaderType value) {
		String id = this.neo4j.createNode(value);
		System.out.println("Added new VEHICLE: "+ id);
		this.id2neo4j.addIdTranslation(value.getId(), id);
		
		// ORIGIN
		if(value.getOrigin() != null) {
			System.out.println("Connection to origin: " + this.id2neo4j.getIdTranslation(value.getOrigin()));
			this.neo4j.connectNodes(
					value.getId(), 
					value.getOrigin(), 
					"comesFrom"
			);
		}
		
		// DESTINATION
		if(value.getDestination() != null) {
			this.neo4j.connectNodes(
					value.getId(), 
					value.getDestination(), 
					"isDirectedTo"
			);
		}
		
		// TODO: retrieve the path from z3
		
		// CURRENT POSITION
		if(value.getPosition() != null) {
			this.neo4j.connectNodes(
					value.getId(), 
					value.getPosition(), 
					"isLocatedIn"
			);
		}
		
		return id;
	}
	
	/**
	 * Function to get a vehicle
	 * @param id
	 * @return the vehicle
	 */
	public VehicleReaderType getVehicle(String id) {
		return this.neo4j.getVehicle(id);
	}

	/**
	 * Function to add a gate as a node in Neo4j
	 * @param value = the gate to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addGate(GateReaderType value) {
		String id = this.neo4j.createNode(value);
		System.out.println("Added new GATE: " + id);
		
		this.id2neo4j.addIdTranslation(value.getId(), id);
		
		// CONTAINER
		if(value.getContainerPlaceId() != null)
			this.neo4j.connectNodes(
					id, 
					this.id2neo4j.getIdTranslation(value.getContainerPlaceId()), 
					"isContainedInto");
		
		// CONNECTED PLACES
		for(String idConnected : value.getConnectedPlaceId()) {
			this.neo4j.connectNodes(
					id, 
					this.id2neo4j.getIdTranslation(idConnected), 
					"isConnectedTo");
		}
		
		return id;
	}
	
	/**
	 * Function to retrieve a specific gate in the system given 
	 * its id
	 * @param gateId = the id of the gate to be retrieved
	 * @return the desired gate
	 */
	public GateReaderType getGate(String gateId) {
		for(GateReaderType gate : this.getGates().getGate()) {
			if(gate.getId().equals(gateId)) return gate;
		}
		
		return null;
	}

	/**
	 * Function to retrieve all the gates loaded into the system
	 * @return a Gates object that contains the list of the gates in the system
	 */
	public Gates getGates() {
		List<GateReaderType> gateList = this.neo4j.getGates();
		Gates gates = (new ObjectFactory()).createGates();
		
		for(GateReaderType gate : gateList) {
			gates.getGate().add(gate);
		}
		
		return gates;
	}
	
	/**
	 * Function to delete a vehicle from the database
	 * @param vehicleId = id of the vehicle to be deleted
	 */
	public void deleteVehicle(String vehicleId) {
		this.neo4j.deleteNode(vehicleId, "Vehicle");
		this.id2neo4j.removeTranslation(vehicleId);
	}

	/**
	 * Function to update the informations of a specific vehicle in the
	 * database
	 * @param vehicle = vehicle information to be updated
	 */
	public void updateVehicle(VehicleReaderType vehicle) {
		this.deleteVehicle(vehicle.getId());
		this.addVehicle(vehicle);
	}

	/**
	 * Function to retrieve all the vehicles in the system
	 * @return a Vehicles object containing all the vehicles
	 */
	public Vehicles getVehicles() {
		List<VehicleReaderType> vehicleList = this.neo4j.getVehicles();
		Vehicles vehicles = (new ObjectFactory()).createVehicles();
		
		for(VehicleReaderType vehicle : vehicleList) {
			vehicles.getVehicle().add(vehicle);
		}
		
		return vehicles;
	}

	public RnsReaderType getSystem() {
		RnsReaderType rns = (new ObjectFactory()).createRnsReaderType();
		
		for(GateReaderType gate : this.neo4j.getGates()) rns.getGate().add(gate);
		for(RoadSegmentReaderType roadSegment : this.neo4j.getRoadSegments()) rns.getRoadSegment().add(roadSegment);
		for(ParkingAreaReaderType parkingArea : this.neo4j.getParkingAreas()) rns.getParkingArea().add(parkingArea);
		for(VehicleReaderType vehicle : this.neo4j.getVehicles()) rns.getVehicle().add(vehicle);
		
		return rns;
	}
}
