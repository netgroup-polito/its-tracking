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
			final String query = StatementBuilder.getInstance().getStatementByType("Gate");
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

	/**
	 * Function to retrieve a particular vehicle from the system
	 * @param vehicleId = id of the vehicle to be retrieved
	 * @return the desired vehicle
	 */
	public synchronized VehicleReaderType getVehicle(String vehicleId) {
		try ( Session session = driver.session() )
        {
			final String query = StatementBuilder.getInstance().getStatementByType("Vehicle", this.id2neo4j.getIdTranslation(vehicleId));
            VehicleReaderType result = session.writeTransaction( new TransactionWork<VehicleReaderType>()
            {
                @Override
                public VehicleReaderType execute( Transaction tx )
                {
                		VehicleReaderType vehicle = (new ObjectFactory()).createVehicleReaderType(); 
					StatementResult result = tx.run(query);
					
					return vehicle;
                }
            } );
            
            return result;
        } catch(Exception e) {
        		e.printStackTrace();
        }
		
		return null;
	}
}