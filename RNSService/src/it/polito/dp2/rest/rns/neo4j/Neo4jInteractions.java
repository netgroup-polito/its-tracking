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
import it.polito.dp2.rest.rns.jaxb.VehicleStateType;
import it.polito.dp2.rest.rns.jaxb.VehicleTypeType;
import it.polito.dp2.rest.rns.utility.Constants;
import it.polito.dp2.rest.rns.utility.DateConverter;
import it.polito.dp2.rest.rns.utility.IdTranslator;

import static org.neo4j.driver.v1.Values.parameters;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;

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
	
	@Override
	protected void finalize() throws Throwable {
		driver.close();
		super.finalize();
	}
	
	/**
	 * Hello world function for test purposes only
	 * @return a string representing the id of the newly created node
	 */
	public synchronized String helloWorld() {
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
	 * Function to create the new node into the db
	 * @param element = the new element to be loaded
	 * @return the corresponding id
	 */
	public String createNode(Object element) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().createStatement(element);
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
	 * Function to connect two nodes given their ids
	 * @param node1 = identifier of source node
	 * @param node2 = identifier of destination node
	 * @param label = label of the relation
	 * @return a string representing the id of the newly created relation
	 */
	public String connectNodes(String node1, String node2, String label) {
		String query = StatementBuilder.getInstance().connectStatement(
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
	
	/**
	 * Function to delete a specific node from the neo4j database
	 * @param nodeId = id of the node to be deleted
	 * @param type = type of the node to be deleted
	 */
	public void deleteNode(String nodeId, String type) {
		String query = StatementBuilder.getInstance().deleteByTypeAndIdStatement(
				this.id2neo4j.getIdTranslation(nodeId),  
				type);
		
		try ( Session session = driver.session() )
        {
            session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                		tx.run(query);
                		return "";
                }
            } );
        }
	}
	
	/**
	 * Function to retrieve from Neo4j all the gates
	 * @return list of the gates currently in the db
	 */
	public synchronized List<GateReaderType> getGates() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndConnection("Gate");
            List<GateReaderType> result = session.writeTransaction( new TransactionWork<List<GateReaderType>>()
            {
                @Override
                public List<GateReaderType> execute( Transaction tx )
                {
                		Map<String, GateReaderType> map = new HashMap<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						GateReaderType gate = (new ObjectFactory()).createGateReaderType();
						
						if(!map.containsKey((String) r.get(0).asMap().get("id"))) {
							gate.setName((String) r.get(0).asMap().get("name"));
							gate.setId((String) r.get(0).asMap().get("id"));
							gate.setType(GateType.fromValue((String) r.get(0).asMap().get("type")));
							gate.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
							gate.getConnectedPlaceId().add(IdTranslator.getInstance().fromNeo4jId(
									String.valueOf((int) r.get(1).asInt())
								));
							
							map.put(
								gate.getId(),
								gate
							);
						} else {
							map.get((String) r.get(0).asMap().get("id"))
								.getConnectedPlaceId()
								.add(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())
									));
						}
					}
					
					return map.values().stream().collect(Collectors.toList());
                }
            } );
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve a particular vehicle from the system
	 * @param vehicleId = id of the vehicle to be retrieved
	 * @return the desired vehicle
	 */
	public synchronized VehicleReaderType getVehicle(String vehicleId) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndId("Vehicle", this.id2neo4j.getIdTranslation(vehicleId));
            VehicleReaderType result = session.writeTransaction( new TransactionWork<VehicleReaderType>()
            {
                @Override
                public VehicleReaderType execute( Transaction tx )
                {
                		VehicleReaderType vehicle = (new ObjectFactory()).createVehicleReaderType(); 
					tx.run(query);
					
					return vehicle;
                }
            } );
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve all the nodes that are of type vehicle
	 * stored in neo4j database
	 * @return the list of vehicles in the system
	 */
	public List<VehicleReaderType> getVehicles() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeNoConnection("Vehicle");
            List<VehicleReaderType> result = session.writeTransaction( new TransactionWork<List<VehicleReaderType>>()
            {
                @Override
                public List<VehicleReaderType> execute( Transaction tx )
                {
                		List<VehicleReaderType> list = new LinkedList<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						VehicleReaderType vehicle = (new ObjectFactory()).createVehicleReaderType();
						
						for( Value entry : r.values()) {
							vehicle.setName((String) entry.asMap().get("name"));
							vehicle.setId((String) entry.asMap().get("id"));
							vehicle.setType(VehicleTypeType.fromValue((String) entry.asMap().get("type")));
							vehicle.setOrigin((String) entry.asMap().get("origin"));
							vehicle.setDestination((String) entry.asMap().get("destination"));
							vehicle.setPosition((String) entry.asMap().get("position"));
							vehicle.setState(VehicleStateType.fromValue((String) entry.asMap().get("state")));
							try {
								vehicle.setEntryTime(DateConverter.convertFromString((String) entry.asMap().get("entryTime"), "yyyy-MM-dd'T'HH:mm:ss"));
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (DatatypeConfigurationException e) {
								e.printStackTrace();
							}
						}
						
						list.add(vehicle);
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

	/**
	 * Function to retrieve all road segments stored in the system
	 * @return the list of road segments
	 */
	public List<RoadSegmentReaderType> getRoadSegments() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndConnectionAndContainer("RoadSegment");
            List<RoadSegmentReaderType> result = session.writeTransaction( new TransactionWork<List<RoadSegmentReaderType>>()
            {
                @Override
                public List<RoadSegmentReaderType> execute( Transaction tx )
                {
                		Map<String, RoadSegmentReaderType> map = new HashMap<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						RoadSegmentReaderType road = (new ObjectFactory()).createRoadSegmentReaderType();
						
						if(!map.containsKey((String) r.get(0).asMap().get("id"))) {
							road.setName((String) r.get(0).asMap().get("name"));
							road.setId((String) r.get(0).asMap().get("id"));
							road.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
							road.getConnectedPlaceId().add(IdTranslator.getInstance().fromNeo4jId(
									String.valueOf((int) r.get(1).asInt())
								));
							
							if(r.get(2) != null) // Check if it has a container
								road.setContainerPlaceId(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(2).asInt())
									));
							
							map.put(
									road.getId(),
									road
							);
						} else {
							map.get((String) r.get(0).asMap().get("id"))
								.getConnectedPlaceId()
								.add(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())
									));
						}
					}
					
					return map.values().stream().collect(Collectors.toList());
                }
            } );
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}

	/**
	 * Function to retrieve all the parking areas stored into the system
	 * @return the list of parking areas
	 */
	public List<ParkingAreaReaderType> getParkingAreas() {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByTypeAndConnection("ParkingArea");
            List<ParkingAreaReaderType> result = session.writeTransaction( new TransactionWork<List<ParkingAreaReaderType>>()
            {
                @Override
                public List<ParkingAreaReaderType> execute( Transaction tx )
                {
                		Map<String, ParkingAreaReaderType> map = new HashMap<>();
					StatementResult result = tx.run(query);
					
					for(Record r : result.list()) {
						ParkingAreaReaderType park = (new ObjectFactory()).createParkingAreaReaderType();
						
						if(!map.containsKey((String) r.get(0).asMap().get("id"))) {
							park.setName((String) r.get(0).asMap().get("name"));
							park.setId((String) r.get(0).asMap().get("id"));
							park.setCapacity(new BigInteger(Long.toString((Long) r.get(0).asMap().get("capacity"))));
							park.getConnectedPlaceId().add(
									IdTranslator.getInstance().fromNeo4jId(
											String.valueOf((int) r.get(1).asInt())
										));
							
							map.put(
									park.getId(),
									park
							);
						} else {
							map.get((String) r.get(0).asMap().get("id"))
								.getConnectedPlaceId()
								.add(IdTranslator.getInstance().fromNeo4jId(
										String.valueOf((int) r.get(1).asInt())
									));
						}
					}
					
					return map.values().stream().collect(Collectors.toList());
                }
            } );
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}
}