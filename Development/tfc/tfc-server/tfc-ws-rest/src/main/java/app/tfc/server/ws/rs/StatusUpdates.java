package app.tfc.server.ws.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/status")
public class StatusUpdates {
	
	private static final Logger l = LogManager.getLogger(StatusUpdates.class);
	
	@GET
	@Path("/countries/{country}/cities/{city}/operators/{operator}/lines")
	@Produces("application/json;charset=utf-8")
	public void status(){
		l.debug("status()::");
		
	}
	
}
