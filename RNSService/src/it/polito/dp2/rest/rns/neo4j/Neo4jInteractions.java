package it.polito.dp2.rest.rns.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import it.polito.dp2.rest.rns.jaxb.ComplexPlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.SimplePlaceReaderType;
import it.polito.dp2.rest.rns.jaxb.VehicleReaderType;

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
	
	public Neo4jInteractions(String uri, String username, String password){
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
		
		if (element instanceof ComplexPlaceReaderType) {
			ComplexPlaceReaderType cp = (ComplexPlaceReaderType) element;
			query += "CREATE (node:ComplexPlace {"
					+ "name: \'" + cp.getComplexPlaceName() + "\',"
					+ "totalCapacity: \'" + cp.getTotalCapacity() + "\'"
					+ "}) " +
                    "RETURN id(node)";
		} else if (element instanceof SimplePlaceReaderType) {
			SimplePlaceReaderType sp = (SimplePlaceReaderType) element;
			query += "CREATE (node:SimplePlace {"
					+ "name: \'" + sp.getSimplePlaceName() + "\',"
					+ "capacity: \'" + sp.getCapacity() + "\'"
					+ "}) " +
                    "RETURN id(node)";
		} else if (element instanceof VehicleReaderType) {
			VehicleReaderType vehicle = (VehicleReaderType) element;
			query += "CREATE (node:Vehicle {"
					+ "name: '" + vehicle.getVehicleName() + "',"
					+ "destination: \'" + vehicle.getDestination() + "\',"
					+ "origin: \'" + vehicle.getOrigin() + "\',"
					+ "position: \'" + vehicle.getPosition() + "\',"
					+ "type: \'" + vehicle.getType() + "\',"
					+ "state: \'" + vehicle.getState() + "\',"
					+ "entryTime: \'" + vehicle.getEntryTime() + "\'"
					+ "}) " +
                    "RETURN id(node)";
		}
		
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
}