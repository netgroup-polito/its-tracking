package it.polito.dp2.rest.rns.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Value;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.GateType;
import it.polito.dp2.rest.rns.jaxb.ObjectFactory;
import it.polito.dp2.rest.rns.jaxb.ParkingAreaReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadReaderType;
import it.polito.dp2.rest.rns.jaxb.RoadSegmentReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.utility.Constants;
import it.polito.dp2.rest.rns.utility.IdTranslator;

import static org.neo4j.driver.v1.Values.parameters;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is responsible to give access to all method necessary to interact 
 * with Neo4j.
 * 
 * @author dp2
 *
 */
public class Neo4jInteractions implements AutoCloseable {
	private Driver driver;
	private IdTranslator id2neo4j;
	private static Neo4jInteractions instance = null;
	
	private Neo4jInteractions(String uri, String username, String password){
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
		this.id2neo4j = IdTranslator.getInstance();
	}
	
	public static Neo4jInteractions getInstance() {
		if(instance == null) {
			instance = new Neo4jInteractions(
					Constants.Neo4jURL, 
					Constants.Neo4jUsername, 
					Constants.Neo4jPassword);
		}
		
		return instance;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}
	
	/**
	 * Hello world function for test purposes only
	 * @return a string representing the id of the newly created node
	 */
	public String helloWorld() {
		try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( 
                    		"CREATE (a:Greeting) " +
            				"SET a.message = $message " +
                            "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", "Hello world!" )
                    );
                    return result.single().get( 0 ).asString();
                }
            } );
            
            return greeting;
        }
	}
	
	/**
	 * Function to create the query according to the type of element it's wanted
	 * to be loaded into the db
	 * @param element = the new element to be loaded
	 * @return a string representing the neo4j query
	 */
	private String createStatement(Object element) {
		String query = "";
		
		if (element instanceof VehicleReaderType) {
			VehicleReaderType vehicle = (VehicleReaderType) element;
			query += "MERGE (vehicle:Vehicle {id: '"+ vehicle.getId() + "'}) "
					+ "ON CREATE SET "
					+ "vehicle.name = '" + vehicle.getVehicleName() + "',"
					+ "vehicle.destination = '" + vehicle.getDestination() + "',"
					+ "vehicle.origin = '" + vehicle.getOrigin() + "',"
					+ "vehicle.position = '" + vehicle.getPosition() + "',"
					+ "vehicle.type = '" + vehicle.getType() + "',"
					+ "vehicle.state = '" + vehicle.getState() + "',"
					+ "vehicle.entryTime = " + vehicle.getEntryTime()
                    + " RETURN id(vehicle)";
		} else if (element instanceof GateReaderType) {
			GateReaderType gate = (GateReaderType) element;
			query += "MERGE (gate:Gate {id: '" + gate.getId() + "'})"
					+ "ON CREATE SET "
					+ "gate.name = '" + gate.getSimplePlaceName() + "',"
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
	 * Function to create the new node into the db
	 * @param element = the new element to be loaded
	 * @return the corresponding id
	 */
	public String createNode(Object element) {
		try ( Session session = driver.session() )
        {
			final String query = this.createStatement(element);
            String nodeId = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run(query);
                    return String.valueOf(result.single().get( 0 ));
                }
            } );
            
            return nodeId;
        }
	}

	/**
	 * Function to retrieve from Neo4j all the gates
	 * @return list of the gates currently in the db
	 */
	public List<GateReaderType> getGates() {
		try ( Session session = driver.session() )
        {
			final String query = "MATCH (gate: Gate) RETURN properties(gate)";
            List<GateReaderType> result = session.writeTransaction( new TransactionWork<List<GateReaderType>>()
            {
                @Override
                public List<GateReaderType> execute( Transaction tx )
                {
                		List<GateReaderType> list = new LinkedList<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						GateReaderType gate = (new ObjectFactory()).createGateReaderType();
						
						for( Value entry : r.values()) {
							gate.setSimplePlaceName((String) entry.asMap().get("name"));
							gate.setId((String) entry.asMap().get("id"));
							gate.setType(GateType.fromValue((String) entry.asMap().get("type")));
							gate.setCapacity(new BigInteger(Long.toString((Long) entry.asMap().get("capacity")))); 
						}
						
						list.add(gate);
					}
					return list;
                }
            } );
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	private String connectStatement(String node1, String node2, String label) {
		String query = 
				"MATCH (n) "
				+ "WHERE id(n) = " + node1 + " "
				+ "MATCH (m) "
				+ "WHERE id(m) = " + node2 + " "
				+ "MERGE (n)-[:" + label + "]->(m) "
				+ "RETURN n, m";
		
		return query;
	}
	
	public String connectNodes(String node1, String node2, String label) {
		String query = this.connectStatement(
				this.id2neo4j.getIdTranslation(node1), 
				this.id2neo4j.getIdTranslation(node2), 
				label);
		
		try ( Session session = driver.session() )
        {
            String relationshipId = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run(query);
                    return String.valueOf(result.single().get( 0 ));
                }
            } );
            
            return relationshipId;
        }
	}
}