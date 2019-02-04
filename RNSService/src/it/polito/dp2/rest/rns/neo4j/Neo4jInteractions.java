package it.polito.dp2.rest.rns.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import it.polito.dp2.rest.rns.jaxb.GateReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;
import it.polito.dp2.rest.rns.utility.Constants;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * This class is responsible to give access to all method necessary to interact 
 * with Neo4j.
 * 
 * @author dp2
 *
 */
public class Neo4jInteractions implements AutoCloseable {
	private Driver driver;
	private static Neo4jInteractions instance = null;
	
	private Neo4jInteractions(String uri, String username, String password){
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}
	
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
	
	private String createStatement(Object element) {
		String query = "";
		
		if (element instanceof VehicleReaderType) {
			VehicleReaderType vehicle = (VehicleReaderType) element;
			query += "CREATE (vehicle:Vehicle {"
					+ "name: '" + vehicle.getVehicleName() + "',"
					+ "destination: '" + vehicle.getDestination() + "',"
					+ "origin: '" + vehicle.getOrigin() + "',"
					+ "position: '" + vehicle.getPosition() + "',"
					+ "type: '" + vehicle.getType() + "',"
					+ "state: '" + vehicle.getState() + "',"
					+ "entryTime: '" + vehicle.getEntryTime() + "'"
					+ "}) " +
                    "RETURN id(vehicle)";
		} else if (element instanceof GateReaderType) {
			GateReaderType gate = (GateReaderType) element;
			query += "CREATE (gate:Gate {"
					+ "name: '" + gate.getSimplePlaceName() + "',"
					+ "capacity: '" + gate.getCapacity() + "',"
					+ "type: '" + gate.getType() + "'"
					+ "}) " +
                    "RETURN id(gate)";
		}
		
		return query;
	}
	
	private String getStatement(String id) {
		String query = "";
		return query;
	}
	
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

	public GateReaderType getGate(String gateId) {
		
		try ( Session session = driver.session() )
        {
			final String query = this.getStatement(gateId);
            String node = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run(query);
                    return String.valueOf(result.single().get( 0 ));
                }
            } );
            
            return null;
        }
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
}