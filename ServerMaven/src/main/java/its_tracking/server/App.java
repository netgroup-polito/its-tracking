package its_tracking.server;

import javax.ws.rs.Path;

@Path("")
public class App 
{
	@GET
	@Path("/rns")
    public String main()
    {
        return "Hello world!";
    }
}
