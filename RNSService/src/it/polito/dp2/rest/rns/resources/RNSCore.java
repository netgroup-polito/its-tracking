package it.polito.dp2.rest.rns.resources;

import java.util.List;
import java.util.stream.Collectors;

import it.polito.dp2.rest.rns.exceptions.InvalidEntryPlaceException;
import it.polito.dp2.rest.rns.exceptions.PlaceFullException;
import it.polito.dp2.rest.rns.exceptions.UnsatisfiableException;
import it.polito.dp2.rest.rns.exceptions.VehicleAlreadyInSystemException;
import it.polito.dp2.rest.rns.exceptions.VehicleNotInSystemException;
import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.Gates;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.Places;
import it.polito.dp2.rest.rns.jaxb.RnsReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.jaxb.Vehicles;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import it.polito.dp2.rest.rns.utility.IdTranslator;
import it.polito.dp2.rest.rns.utility.MapLoader;
import it.polito.dp2.rest.rns.z3.Z3;

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
	//private Graph actualMap;
	
	/**
	 * Private constructor, so that the instance of the object can only be accessed
	 * via static method getInstance()
	 */
	private RNSCore(){
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
	 * @throws UnsatisfiableException 
	 */
	public Places addVehicle(VehicleReaderType vehicle) throws PlaceFullException, VehicleAlreadyInSystemException, InvalidEntryPlaceException, UnsatisfiableException {
		String id = "";
		System.out.println("***************** ADD VEHICLE *********************");
		// CURRENT POSITION
		if(vehicle.getPosition() != null) {
			// Check presence of the vehicle in the system
			List<String> vehiclesLoadedIds = 
					Neo4jInteractions.getInstance().getVehicles()
							.stream()
							.map(VehicleReaderType::getId)
							.collect(Collectors.toList());
			if(vehiclesLoadedIds.contains(vehicle.getId())) throw(new VehicleAlreadyInSystemException("Vehicle " + vehicle.getId() + " has already been added to the system."));
			
			Z3 z3 = new Z3(vehicle.getOrigin(), vehicle.getDestination(), vehicle.getMaterial().get(0));
			List<String> path = z3.findPath();
			
			if(path != null) {
				// Check if the ORIGIN is a gate of type IN or INOUT
				GateReaderType origin = this.getGate(vehicle.getOrigin());
				if(origin != null) {
					if(origin.getType().toString().equals("OUT"))
						throw(new InvalidEntryPlaceException("The gate " + vehicle.getOrigin() +" you're trying to enter the system from, is not of type IN, neither INOUT."));
				}
				id = Neo4jInteractions.getInstance().createNode(vehicle);
				//System.out.println("Added new VEHICLE: "+ id);
				IdTranslator.getInstance().addIdTranslation(vehicle.getId(), id);
				
				// ORIGIN
				if(vehicle.getOrigin() != null) {
					//System.out.println("Connection to origin: " + this.id2neo4j.getIdTranslation(vehicle.getOrigin()));
					Neo4jInteractions.getInstance().connectNodes(
							vehicle.getId(), 
							vehicle.getOrigin(), 
							"comesFrom"
					);
				}
				
				// DESTINATION
				if(vehicle.getDestination() != null) {
					Neo4jInteractions.getInstance().connectNodes(
							vehicle.getId(), 
							vehicle.getDestination(), 
							"isDirectedTo"
					);
				}
				
				// CURRENT POSITION
				Neo4jInteractions.getInstance().connectNodes(
						vehicle.getId(), 
						vehicle.getPosition(), 
						"isLocatedIn"
				);
				
				// MATERIAL TRANSPORTED IF ANY
				if(vehicle.getMaterial() != null) {
					if(vehicle.getMaterial().size() >= 1) {
						for(String material : vehicle.getMaterial())
							Neo4jInteractions.getInstance().connectNodes(
									vehicle.getId(), 
									material, 
									"transports"
							);
					}
				}
				
				return this.orderPlaces(path, vehicle.getDestination(), vehicle.getOrigin());
			} else {
				throw new UnsatisfiableException("A path couldn't be found.");
			}
		} else {
			throw new UnsatisfiableException("Current vehicle place can't be null.");
		}
	}
	
	/**
	 * Function to get a vehicle
	 * @param id
	 * @return the vehicle
	 * @throws VehicleNotInSystemException 
	 */
	public VehicleReaderType getVehicle(String id) throws VehicleNotInSystemException {
		return Neo4jInteractions.getInstance().getVehicle(id);
	}

	/**
	 * Function to add a gate as a node in Neo4j
	 * @param value = the gate to be added
	 * @return the id of the node, assigned automatically by Neo4j
	 */
	public String addGate(GateReaderType value) {
		String id = Neo4jInteractions.getInstance().createNode(value);
		System.out.println("Added new GATE: " + id);
		
		IdTranslator.getInstance().addIdTranslation(value.getId(), id);
		
		// CONTAINER
		if(value.getContainerPlaceId() != null)
			Neo4jInteractions.getInstance().connectNodes(
					id, 
					IdTranslator.getInstance().getIdTranslation(value.getContainerPlaceId()), 
					"isContainedInto");
		
		// CONNECTED PLACES
		for(String idConnected : value.getConnectedPlaceId()) {
			Neo4jInteractions.getInstance().connectNodes(
					id, 
					IdTranslator.getInstance().getIdTranslation(idConnected), 
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
		List<GateReaderType> gateList = Neo4jInteractions.getInstance().getGates();
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
		Neo4jInteractions.getInstance().deleteNode(vehicleId, "Vehicle");
		IdTranslator.getInstance().removeTranslation(vehicleId);
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
				Neo4jInteractions.getInstance().getVehicles()
						.stream()
						.map(VehicleReaderType::getId)
						.collect(Collectors.toList());
		if(!vehiclesLoadedIds.contains(vehicle.getId())) throw(new VehicleNotInSystemException("Vehicle " + vehicle.getId() + " is not currently in the system."));
		
		// TODO: if the place is in the path of the vehicle OK, otherwise need to recompute
		// the path
		
		// Check on the capacity of the place
		int actualCapacityOfPlace = Neo4jInteractions.getInstance().getActualCapacityOfPlace(vehicle.getPosition());
		if(actualCapacityOfPlace < 1) throw(new PlaceFullException("Place " + vehicle.getPosition() + " is full. It can't accept any more vehicles."));
		
		// Can update
		System.out.println("*************** UPDATE VEHICLE POSITION ****************");
		System.out.println("Updating position of vehicle " + vehicle.getId());
		VehicleReaderType currentVehicle = Neo4jInteractions.getInstance().getVehicle(vehicle.getId());
		System.out.println("\tFROM: " + currentVehicle.getPosition());
		System.out.println("\tTO: " + vehicle.getPosition());
		Neo4jInteractions.getInstance().updatePositionVehicle(vehicle, currentVehicle.getPosition());
	}

	/**
	 * Function to retrieve all the vehicles in the system
	 * @return a Vehicles object containing all the vehicles
	 */
	public Vehicles getVehicles() {
		List<VehicleReaderType> vehicleList = Neo4jInteractions.getInstance().getVehicles();
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
		
		for(GateReaderType gate : Neo4jInteractions.getInstance().getGates()) rns.getGate().add(gate);
		for(RoadSegmentReaderType roadSegment : Neo4jInteractions.getInstance().getRoadSegments()) rns.getRoadSegment().add(roadSegment);
		for(ParkingAreaReaderType parkingArea : Neo4jInteractions.getInstance().getParkingAreas()) rns.getParkingArea().add(parkingArea);
		for(VehicleReaderType vehicle : Neo4jInteractions.getInstance().getVehicles()) rns.getVehicle().add(vehicle);
		
		return rns;
	}
	
	private Places orderPlaces(List<String> path, String destinationId, String sourceId) {
		// I HAVE TO RETURN IN ORDER THE PLACES
		Places places = (new ObjectFactory()).createPlaces();
		String currentId = path.stream().filter((place) -> place.equals(sourceId)).findFirst().get();
		SimplePlaceReaderType current = null;
		
		while(!path.isEmpty() && !currentId.equals(destinationId)) {
			current = Neo4jInteractions.getInstance().getPlace(currentId);
			
			for(String s : current.getConnectedPlaceId()) {
				if(!currentId.equals(s) && path.contains(s)) {
					path.remove(currentId);
					places.getPlace().add(current);
					currentId = s;
				}
			}
		}
		
		places.getPlace().add(Neo4jInteractions.getInstance().getPlace(currentId));
		
		return places;
	}
}
