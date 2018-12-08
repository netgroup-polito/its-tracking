package it.polito.dp2.rest.rns.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * This class is responsible to give access to all method necessary to interact 
 * with Neo4j.
 * 
 * @author dp2
 *
 */
public class Neo4jInteractions implements AutoCloseable{
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
}
