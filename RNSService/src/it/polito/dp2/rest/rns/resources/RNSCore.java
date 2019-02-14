package it.polito.dp2.rest.rns.resources;

import java.util.List;
import java.util.stream.Collectors;

import it.polito.dp2.rest.rns.exceptions.InvalidEntryPlaceException;
import it.polito.dp2.rest.rns.exceptions.PlaceFullException;
import it.polito.dp2.rest.rns.exceptions.VehicleAlreadyInSystemException;
import it.polito.dp2.rest.rns.exceptions.VehicleNotInSystemException;
import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.Gates;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RnsReaderType;
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
	//private Graph actualMap;
	
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
		return "Hello world!";
	}
	
	/**
	 * Function to add a vehicle as a node, both in the local map
	 * database and in Neo4j
	 * @param value = the vehicle to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 * @throws PlaceFullException --> if the vehicle can't be added to the system
	 * @throws VehicleAlreadyInSystemException --> id the vehicle has already been added into the system
	 * @throws InvalidEntryPlaceException --> if the vehicle is trying to access the system from a gate that isn't of type IN or INOUT
	 */
	public String addVehicle(VehicleReaderType vehicle) throws PlaceFullException, VehicleAlreadyInSystemException, InvalidEntryPlaceException {
		String id = "";
		System.out.println("***************** ADD VEHICLE *********************");
		// CURRENT POSITION
		if(vehicle.getPosition() != null) {
			// Check presence of the vehicle in the system
			List<String> vehiclesLoadedIds = 
					this.neo4j.getVehicles()
							.stream()
							.map(VehicleReaderType::getId)
							.collect(Collectors.toList());
			if(vehiclesLoadedIds.contains(vehicle.getId())) throw(new VehicleAlreadyInSystemException("Vehicle " + vehicle.getId() + " has already been added to the system."));
			
			// Check on the capacity of the place
			int actualCapacityOfPlace = this.neo4j.getActualCapacityOfPlace(vehicle.getPosition());
			if(actualCapacityOfPlace < 1) throw(new PlaceFullException("Place " + vehicle.getPosition() + " is full. It can't accept any more vehicles."));
			
			// Check if the ORIGIN is a gate of type IN or INOUT
			GateReaderType origin = this.getGate(vehicle.getOrigin());
			if(origin != null) {
				if(origin.getType().toString().equals("OUT"))
					throw(new InvalidEntryPlaceException("The gate " + vehicle.getOrigin() +" you're trying to enter the system from, is not of type IN, neither INOUT."));
			}
			id = this.neo4j.createNode(vehicle);
			System.out.println("Added new VEHICLE: "+ id);
			this.id2neo4j.addIdTranslation(vehicle.getId(), id);
			
			// ORIGIN
			if(vehicle.getOrigin() != null) {
				System.out.println("Connection to origin: " + this.id2neo4j.getIdTranslation(vehicle.getOrigin()));
				this.neo4j.connectNodes(
						vehicle.getId(), 
						vehicle.getOrigin(), 
						"comesFrom"
				);
			}
			
			// DESTINATION
			if(vehicle.getDestination() != null) {
				this.neo4j.connectNodes(
						vehicle.getId(), 
						vehicle.getDestination(), 
						"isDirectedTo"
				);
			}
			
			this.neo4j.connectNodes(
					vehicle.getId(), 
					vehicle.getPosition(), 
					"isLocatedIn"
			);
		}
		
		// TODO: retrieve the path from z3
		
		return id;
	}
	
	/**
	 * Function to get a vehicle
	 * @param id
	 * @return the vehicle
	 * @throws VehicleNotInSystemException 
	 */
	public VehicleReaderType getVehicle(String id) throws VehicleNotInSystemException {
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
	 * @throws PlaceFullException 
	 * @throws VehicleNotInSystemException 
	 */
	public void updateVehicle(VehicleReaderType vehicle) throws VehicleNotInSystemException, PlaceFullException {
		// Check presence of the vehicle in the system
		List<String> vehiclesLoadedIds = 
				this.neo4j.getVehicles()
						.stream()
						.map(VehicleReaderType::getId)
						.collect(Collectors.toList());
		if(!vehiclesLoadedIds.contains(vehicle.getId())) throw(new VehicleNotInSystemException("Vehicle " + vehicle.getId() + " is not currently in the system."));
		
		// TODO: if the place is in the path of the vehicle OK, otherwise need to recompute
		// the path
		
		// Check on the capacity of the place
		int actualCapacityOfPlace = this.neo4j.getActualCapacityOfPlace(vehicle.getPosition());
		if(actualCapacityOfPlace < 1) throw(new PlaceFullException("Place " + vehicle.getPosition() + " is full. It can't accept any more vehicles."));
		
		// Can update
		System.out.println("*************** UPDATE VEHICLE POSITION ****************");
		System.out.println("Updating position of vehicle " + vehicle.getId());
		VehicleReaderType currentVehicle = this.neo4j.getVehicle(vehicle.getId());
		System.out.println("\tFROM: " + currentVehicle.getPosition());
		System.out.println("\tTO: " + vehicle.getPosition());
		this.neo4j.updatePositionVehicle(vehicle, currentVehicle.getPosition());
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

	/**
	 * Function to retrieve all informations currently stored 
	 * in the database.
	 * @return an object containing such information
	 */
	public RnsReaderType getSystem() {
		RnsReaderType rns = (new ObjectFactory()).createRnsReaderType();
		
		for(GateReaderType gate : this.neo4j.getGates()) rns.getGate().add(gate);
		for(RoadSegmentReaderType roadSegment : this.neo4j.getRoadSegments()) rns.getRoadSegment().add(roadSegment);
		for(ParkingAreaReaderType parkingArea : this.neo4j.getParkingAreas()) rns.getParkingArea().add(parkingArea);
		for(VehicleReaderType vehicle : this.neo4j.getVehicles()) rns.getVehicle().add(vehicle);
		
		return rns;
	}
}
