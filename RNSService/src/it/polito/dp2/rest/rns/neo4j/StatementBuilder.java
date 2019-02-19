package it.polito.dp2.rest.rns.neo4j;

import java.util.List;

import it.polito.dp2.rest.rns.jaxb.DangerousMaterialType;
import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.ServiceType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;

public class StatementBuilder {
	private static StatementBuilder instance = null;
	
	private StatementBuilder() {};
	
	public static StatementBuilder getInstance() {
		if(instance == null) instance = new StatementBuilder();
		return instance;
	}
	
	/**
	 * Function to create a statement to be used to instantiate a relation
	 * between two nodes
	 * @param node1 = identifier of source node
	 * @param node2 = identifier of destination node
	 * @param label = label of the relation
	 * @return a string representing the query
	 */
	public String connectStatement(String node1, String node2, String label) {
		String query = 
				"MATCH (n) "
				+ "WHERE id(n) = " + node1 + " "
				+ "MATCH (m) "
				+ "WHERE id(m) = " + node2 + " "
				+ "MERGE (n)-[r:" + label + "]->(m) "
				+ "RETURN id(r)";
		
		return query;
	}
	
	/**
	 * Function to create the query according to the type of element it's wanted
	 * to be loaded into the db
	 * @param element = the new element to be loaded
	 * @return a string representing the neo4j query
	 */
	public String createStatement(Object element) {
		String query = "";
		
		if (element instanceof VehicleReaderType) {
			VehicleReaderType vehicle = (VehicleReaderType) element;
			query += "MERGE (vehicle:Vehicle {id: '"+ vehicle.getId() + "'}) "
					+ "ON CREATE SET "
					+ "vehicle.name = '" + vehicle.getName() + "',"
					+ "vehicle.destination = '" + vehicle.getDestination() + "',"
					+ "vehicle.origin = '" + vehicle.getOrigin() + "',"
					+ "vehicle.position = '" + vehicle.getPosition() + "',"
					+ "vehicle.type = '" + vehicle.getType() + "',"
					+ "vehicle.state = '" + vehicle.getState() + "',"
					+ "vehicle.entryTime = '" + vehicle.getEntryTime() + "'"
                    + " RETURN id(vehicle)";
		} else if (element instanceof GateReaderType) {
			GateReaderType gate = (GateReaderType) element;
			query += "MERGE (gate:Gate {id: '" + gate.getId() + "'})"
					+ "ON CREATE SET "
					+ "gate.name = '" + gate.getName() + "',"
					+ "gate.capacity = " + gate.getCapacity() + ","
					+ "gate.avgTimeSpent = " + gate.getAvgTimeSpent() + ","
					+ "gate.type = '" + gate.getType() + "'"
                    + " RETURN id(gate)";
		} else if (element instanceof RoadSegmentReaderType) {
			RoadSegmentReaderType roadSegment = (RoadSegmentReaderType) element;
			
			query += "MERGE (roadSegment: RoadSegment {id: '" + roadSegment.getId() + "'}) "
					+ "ON CREATE SET roadSegment.name = '" + roadSegment.getName() + "',"
					+ "roadSegment.capacity = " + roadSegment.getCapacity().intValue() + ","
					+ "roadSegment.avgTimeSpent = " + roadSegment.getAvgTimeSpent().intValue()
					+ " RETURN id(roadSegment)";
		} else if (element instanceof RoadReaderType) {
			RoadReaderType road = (RoadReaderType) element;
			
			query += "MERGE (road: Road {id: '" + road.getId() + "'}) "
					+ "ON CREATE SET road.name = '" + road.getName() + "'"
					+ " RETURN id(road)";
		} else if (element instanceof ParkingAreaReaderType) {
			ParkingAreaReaderType park = (ParkingAreaReaderType) element;
			
			query += "MERGE (park: ParkingArea {id: '" + park.getId() + "'}) "
					+ "ON CREATE SET park.capacity = " + park.getCapacity() + ", "
					+ "park.name = '" + park.getName() + "', "
					+ "park.avgTimeSpent = " + park.getAvgTimeSpent().intValue() + ", "
					+ "park.service = " + this.getCorrectStringFromListService(park.getService())
					+ " RETURN id(park)";
		} else if (element instanceof DangerousMaterialType) {
			DangerousMaterialType material = (DangerousMaterialType) element;
			
			query += "MERGE (material: DangerousMaterial {id: '" + material.getId() + "'}) "
					+ " RETURN id(material)";
		}
		
		return query;
	}
	
	private String getCorrectStringFromListService(List<ServiceType> serviceList) {
		String result = "[";
		
		int i = 0;
		for(ServiceType service : serviceList) {
			result += "'" + service.getName() + "'";
			if(i < serviceList.size() - 1) result += ", ";
			i++;
		}
		
		result += "]";
		result.trim();
		return result;
	}

	/**
	 * Function to retrieve all the nodes of a certain type
	 * with all the connections
	 * @param type = type of the nodes to be retrieved
	 * @return a string corresponding to the desired query
	 */
	public String getStatementByTypeAndConnection(String type) {
		String query = "MATCH (n: " + type + ")-[:isConnectedTo*1]->(connected) RETURN properties(n), id(connected)";
		return query;
	}
	
	/**
	 * Function to retrieve all the nodes of a certain type
	 * with all the connections
	 * @param type = type of the nodes to be retrieved
	 * @return a string corresponding to the desired query
	 */
	public String getStatementByTypeAndConnection(String type, String typeConnection) {
		String query = "MATCH (n: " + type + ")-[:" + typeConnection + "*0..1]->(connected) RETURN properties(n), id(connected)";
		return query;
	}
	
	/**
	 * Function to retrieve all the nodes of a certain type
	 * with all the connections and relations with containers
	 * @param type = type of the nodes to be retrieved
	 * @return a string corresponding to the desired query
	 */
	public String getStatementByTypeAndConnectionAndContainer(String type) {
		String query = 
				"MATCH (n: " + type + ")-[:isConnectedTo*0..1]->(connected) "
				+ "MATCH (n: " + type + " )-[:isContainedInto*0..1]->(container) "
				+ "WHERE id(n) <> id(connected) AND "
				+ "id(n) <> id(container) "
				+ "RETURN properties(n), id(connected), id(container)";
		return query;
	}
	
	/**
	 * Function to retrieve all the nodes of a certain type
	 * @param type = type of the nodes to be retrieved
	 * @return a string corresponding to the desired query
	 */
	public String getStatementByTypeNoConnection(String type) {
		String query = "MATCH (n: " + type + ") RETURN properties(n)";
		return query;
	}
	
	/**
	 * Function to retrieve the node of a certain type and with a certain id
	 * @param type = type of the node to be retrieved
	 * @param id = id of the node to be retrieved
	 * @return a string corresponding to the desired query
	 */
	public String getStatementByTypeAndId(String type, String id) {
		String query = "MATCH (n: " + type + ") WHERE id(n) = " + id + " RETURN properties(n)";
		return query;
	}

	/**
	 * Function to create a statement to delete a specific node based on its type
	 * and having a specific id
	 * @param id = id of the node to be deleted
	 * @param type = type of the node to be deleted
	 * @return a string containing the corresponding query
	 */
	public String deleteByTypeAndIdStatement(String id, String type) {
		String query = "MATCH (n: " + type + ") WHERE id(n) = " + Integer.parseInt(id) + " DETACH DELETE (n)";
		return query;
	}
	
	/**
	 * Function to delete all nodes of a certain type from the database
	 * @param type = type of the nodes to be deleted
	 * @return a string corresponding to the query to delete all nodes of a certain type
	 */
	public String deleteByTypeStatement(String type) {
		String query = "MATCH (n: " + type + ") DETACH DELETE (n)";
		return query;
	}
	
	/**
	 * Function to create a statement to delete a specific relation
	 * @param idSource = id of the source node
	 * @param idDestination = id of the destination node
	 * @param nameRelation = name of the relation
	 * @return a string corresponding to the query to be run
	 */
	public String deleteRelation(String idSource, String idDestination, String nameRelation) {
		String query = "MATCH (n)-[r:" + nameRelation + "]->(m) "
				+ "WHERE id(n)=" + idSource + " " 
				+ "AND id(m)=" + idDestination + " "
				+ "DELETE r";
		return query;
	}

	/**
	 * Function to get the assigned id to a node whose client-side id and 
	 * type are known
	 * @param id= client-side id of the node, in neo4j is a property
	 * @param type = type of the node
	 * @return the corresponding neo4j id
	 */
	public String getIdStatementByTypeAndId(String id, String type) {
		String query = "MATCH (n: " + type + " {id: '" + id + "' }) RETURN id(n)";
		return query;
	}

	/**
	 * Function to update the position of a vehicle in the database
	 * @param id = id of the vehicle node. Here we need to use CLIENT-SIDE id
	 * 			because we can't use MERGE with id()
	 * @param newPosition = new position to be set as property
	 * @return the corresponding query
	 */
	public String updatePositionVehicle(String id, String newPosition, String newState) {
		String query = "MATCH (n: Vehicle) "
				+ "WHERE id(n) = " + id + " "
				+ "SET n.position = '" + newPosition + "' "
				+ "SET n.state = '" + newState + "' "
				+ "RETURN id(n)";
		return query;
	}

	/**
	 * Function to retrieve a single node with its connected nodes
	 * given its id.
	 * @param id = id of the node to be retrieved
	 * @return the corresponding statement
	 */
	public String getNodeById(String id) {
		String query = "MATCH(n)-[:isConnectedTo*0..1]->(c) "
				+ "WHERE id(n) = " + id + " "
				+ "RETURN n, c";
		return query;
	}

	/**
	 * Function to get a query in order to retrieve the actual capacity of 
	 * a node in the database
	 * @param id = id of the node in question
	 * @return the corresponding query
	 */
	public String getActualCapacityStatementById(String id) {
		String query = "MATCH(n) "
				+ "WHERE id(n) = " + id + " "
				+ "OPTIONAL MATCH (n)<-[r:isLocatedIn]-() "
				+ "RETURN n.capacity, COUNT(r)";
		return query;
	}
	
	/**
	 * Function to retrieve the list of incompatible materials with the
	 * one the id is given as parameter.
	 * @param id = id of the material we want to retrieve the incompatible
	 * @return the corresponding query
	 */
	public String getIncompatibleMaterialsStatementById(String id) {
		String query = "MATCH (n: DangerousMaterial)-[r:isIncompatibleWith*1]->(m) "
				+ "WHERE id(n) = " + id + " "
				+ "RETURN m.id as id";
		
		return query;
	}
	
	/**
	 * Function to create the statement to retrieve all the dangerous materials into
	 * specific place
	 * @param id = id of the place
	 * @return the corresponding query
	 */
	public String getMaterialInPlaceStatementById(String id) {
		String query = "MATCH (n)<-[r:isLocatedIn]-(v:Vehicle)-[t:transports]->(m) "
				+ "WHERE id(n) = " + id + " "
				+ "RETURN m.id as id";
		return query;
	}
	
	/**
	 * Function to obtain a query for retrieving the average time 
	 * of a place, given its id.
	 * @param id = id of the considered place
	 * @return the corresponding query
	 */
	public String getAvarageTimeById(String id) {
		String query = "MATCH (n) WHERE id(n) = " + id + " "
				+ "RETURN n.avgTimeSpent";
		return query;
	}

	/**
	 * Function to obtain a query to retrieve the labels of
	 * a specific node given its id
	 * @param id = the id of the node we want
	 * @return the corresponding query
	 */
	public String getLabelOfNodeById(String id) {
		String query = "MATCH (n) WHERE id(n) = " + id + " "
				+ "RETURN labels(n)";
		return query;
	}

	/**
	 * Function to get statement to decrease capacity of node given
	 * its id
	 * @param id = id of the node
	 * @return the corresponding query
	 */
	public String getIncreaseStatementById(String id) {
		String query = "MATCH (n) WHERE id(n) = " + id + " "
				+ "SET n.capacity = n.capacity + 1 "
				+ "RETURN n";
		return query;
	}
	
	/**
	 * Function to retrieve a statement to decrease the capacity of
	 * a node given its id
	 * @param id = id of the node
	 * @return the corresponding statement
	 */
	public String getDecreaseStatementById(String id) {
		String query = "MATCH (n) WHERE id(n) = " + id + " "
				+ "SET n.capacity = n.capacity - 1 "
				+ "RETURN n";
		return query;
	}
}
