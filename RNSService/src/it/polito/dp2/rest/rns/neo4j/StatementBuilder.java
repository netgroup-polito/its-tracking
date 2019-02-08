package it.polito.dp2.rest.rns.neo4j;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
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
					+ "gate.type = '" + gate.getType() + "'"
                    + " RETURN id(gate)";
		} else if (element instanceof RoadSegmentReaderType) {
			RoadSegmentReaderType roadSegment = (RoadSegmentReaderType) element;
			
			query += "MERGE (roadSegment: RoadSegment {id: '" + roadSegment.getId() + "'}) "
					+ "ON CREATE SET roadSegment.name = '" + roadSegment.getName() + "',"
					+ "roadSegment.capacity = " + roadSegment.getCapacity().intValue() + ","
					+ "roadSegment.avgTimeSpent = " + roadSegment.getAvgTimeSpent()
					+ " RETURN id(roadSegment)";
		} else if (element instanceof RoadReaderType) {
			RoadReaderType road = (RoadReaderType) element;
			
			query += "MERGE (road: Road {id: '" + road.getId() + "'}) "
					+ "ON CREATE SET road.name = '" + road.getName() + "'"
					+ " RETURN id(road)";
		} else if (element instanceof ParkingAreaReaderType) {
			ParkingAreaReaderType park = (ParkingAreaReaderType) element;
			
			query += "MERGE (park: ParkingArea {id: '" + park.getId() + "'}) "
					+ "ON CREATE SET park.capacity = " + park.getCapacity() + ","
					+ "park.avgTimeSpent = " + park.getAvgTimeSpent()
					+ " RETURN id(park)";
		}
		
		return query;
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
	 * with all the connections and relations with containers
	 * @param type = type of the nodes to be retrieved
	 * @return a string corresponding to the desired query
	 */
	public String getStatementByTypeAndConnectionAndContainer(String type) {
		String query = 
				"MATCH (n: " + type + ")-[:isConnectedTo*1]->(connected) "
				+ "MATCH (n: " + type + " )-[:isContainedInto*1]->(container) "
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
	 * Function to obtain a query to update the information of a specific ndoe
	 * in the database
	 * @param element = element information to be updated
	 * @return a string corresponding the the requested query
	 */

	public String updateStatement(Object element) {
		return null;
	}
}
