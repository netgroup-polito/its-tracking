package it.polito.dp2.rest.rns.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.neo4j.Neo4jInteractions;
import it.polito.dp2.rest.rns.utility.Constants;
import it.polito.dp2.rest.rns.utility.DangerousMaterialImpl;
import it.polito.dp2.rest.rns.utility.IdTranslator;

public class Graph {
	private ConcurrentHashMap<String, List<VehicleReaderType>> placeVehicles;
	private ConcurrentHashMap<String, Integer> placeAverageTime;
	private ConcurrentHashMap<String, Integer> placeMaxCountVehicles;
	private Map<String, DangerousMaterialImpl> dangerousMaterials;
	private List<String> vehicleIdsInSystem;
	private Neo4jInteractions neo4j;
	
	private static Graph instance = null;
	
	private Graph() {
		this.placeVehicles = new ConcurrentHashMap<>();
		this.placeAverageTime = new ConcurrentHashMap<>();
		this.placeMaxCountVehicles = new ConcurrentHashMap<>();
		this.vehicleIdsInSystem = new ArrayList<>();
		this.neo4j = Neo4jInteractions.getInstance();
		
		this.placeVehicles.keySet().stream().forEach((placeId) -> {
			this.placeVehicles.put(placeId, new ArrayList<>());
		});
		
		this.dangerousMaterials = new HashMap<>();
		
		// Add all dangerous materials
		this.dangerousMaterials.put(Constants.dangerousMaterial1, new DangerousMaterialImpl(Constants.dangerousMaterial1, Constants.notCompatibleWith1));
		this.dangerousMaterials.put(Constants.dangerousMaterial2, new DangerousMaterialImpl(Constants.dangerousMaterial2, Constants.notCompatibleWith2));
		this.dangerousMaterials.put(Constants.dangerousMaterial3, new DangerousMaterialImpl(Constants.dangerousMaterial3, Constants.notCompatibleWith3));
		this.dangerousMaterials.put(Constants.dangerousMaterial4, new DangerousMaterialImpl(Constants.dangerousMaterial4, Constants.notCompatibleWith4));
	}
	
	public static Graph getInstance() {
		if(instance == null)  instance = new Graph();
		
		return instance;
	}
	
	//************************************************************//
	//															//
	//							PLACES							//
	//															//
	//************************************************************//
	public void addPlace(String placeId, int avgTime, List<VehicleReaderType> vehicles, int capacity) {
		//System.out.println("Added place " + placeId + " --- avgTime = " + avgTime + " ---  capacity = " + capacity);
		this.placeVehicles.put(placeId, (vehicles == null) ? new ArrayList<>() : vehicles);
		this.placeAverageTime.put(placeId, (avgTime == 0) ? 0 : avgTime);
		this.placeMaxCountVehicles.put(placeId, capacity);
	}
	
	public void removePlace(String placeId) {
		this.placeAverageTime.remove(placeId);
		this.placeMaxCountVehicles.remove(placeId);
		this.placeVehicles.remove(placeId);
	}
	
	public void evaluateNewAverageTimeForPlace(String placeId, int duration) {
		int oldCount = this.placeVehicles.get(placeId).size();
		int oldSum = this.placeAverageTime.get(placeId);
		
		this.placeAverageTime.put(placeId, (duration + oldSum) / oldCount);
	}
	
	public int getAvgTimeForPlace(String placeId) {
		return this.placeAverageTime.get(placeId);
	}
	
	//************************************************************//
	//															//
	//						 VEHICLES						 	//
	//															//
	//************************************************************//
	public void addvehicleToPlace(String placeId, VehicleReaderType vehicle) throws PlaceFullException {
		// TODO: check for each node of the computed path if the vehicle fits in
		if(this.placeMaxCountVehicles.get(placeId) > this.placeVehicles.get(placeId).size()) {
			this.placeVehicles.get(placeId).add(vehicle);
			this.vehicleIdsInSystem.add(vehicle.getId());
		} else {
			throw(new PlaceFullException());
		}
	}
	
	public void removeVehicleFromPlace(String placeId, String vehicleId) {
		System.out.println("Removing " + vehicleId + " from " + placeId);
		if(this.vehicleIdsInSystem.contains(vehicleId)) {
			int index = 0;
			for(VehicleReaderType vehicle : this.placeVehicles.get(placeId)) {
				if(vehicle.getId().equals(vehicleId)) break;
				else index++;
			}
			this.placeVehicles.get(placeId).remove(index);
			this.vehicleIdsInSystem.remove(vehicleId);
		}
	}
	
	public List<VehicleReaderType> getVehiclesInPlace(String placeId) {
		return this.placeVehicles.get(placeId);
	}

	public VehicleReaderType getVehicle(String id) throws VehicleNotInSystemException {
		if(this.vehicleIdsInSystem.contains(id))
			return this.neo4j.getVehicle(id);
		else
			throw(new VehicleNotInSystemException());
	}

	public void updateVehiclePosition(VehicleReaderType vehicle) throws VehicleNotInSystemException, PlaceFullException {
		if(!this.vehicleIdsInSystem.contains(vehicle.getId())) { 
			System.out.println("Vehicle not in the system: " + vehicle.getId());
			throw(new VehicleNotInSystemException()); 
		}
		
		if(this.placeMaxCountVehicles.get(vehicle.getPosition()) <= this.placeVehicles.get(vehicle.getPosition()).size()) {
			System.out.println(
					"Not enough space in place " + vehicle.getPosition() 
					+ " max = " + this.placeMaxCountVehicles.get(vehicle.getPosition())
					+ " current = " + this.placeVehicles.get(vehicle.getPosition()).size());
			throw(new PlaceFullException());
		}
		
		// Everything it's alright
		VehicleReaderType currentVehicle = this.neo4j.getVehicle(vehicle.getId());
		
		this.removeVehicleFromPlace(currentVehicle.getPosition(), vehicle.getId());
		this.addvehicleToPlace(vehicle.getPosition(), vehicle);
		
		System.out.println("**************************************");
		System.out.println("Moving " + vehicle.getId() + " from " +  currentVehicle.getPosition() + " to " + vehicle.getPosition());
		this.neo4j.updatePositionVehicle(vehicle, currentVehicle.getPosition());
	}
	
	public void loadVehiclesInSystem() {
		List<VehicleReaderType> vehicles = this.neo4j.getVehicles();
		
		if(vehicles == null) return;
		
		vehicles.stream().forEach((vehicle) -> {
			this.placeVehicles.get(vehicle.getPosition()).add(vehicle);
			this.vehicleIdsInSystem.add(vehicle.getId());
			String neo4jId = this.neo4j.getNeo4jNodeId(vehicle.getId(), "Vehicle");
			IdTranslator.getInstance().addIdTranslation(vehicle.getId(), neo4jId);
		});
	}
}
