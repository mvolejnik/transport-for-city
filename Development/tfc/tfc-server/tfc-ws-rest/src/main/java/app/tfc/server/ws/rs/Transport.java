package app.tfc.server.ws.rs;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import app.tfc.server.management.Metrics;
import app.tfc.server.model.Cities;
import app.tfc.server.model.Countries;
import app.tfc.server.model.Lines;
import app.tfc.server.model.Operators;
import app.tfc.server.ws.json.JsonIdentifiables;

import static app.tfc.server.management.Metrics.MetricsNames.*;

@Path("/transport")
public class Transport {
	
	private static final Timer T_CITIES = Metrics.REGISTRY.timer(MetricRegistry.name(Transport.class, METRICS_TRANSPORT_CITIES.getName()));
	private static final Timer T_LINES = Metrics.REGISTRY.timer(MetricRegistry.name(Transport.class, METRICS_TRANSPORT_LINES.getName()));

	private static final Logger l = LogManager.getLogger(Transport.class);
	
	JsonIdentifiables identifialbes = new JsonIdentifiables();

	@GET
	@Path("/cities")
	@Produces("application/json;charset=utf-8")
	//@Timed(name=METRICS_TRANSPORT_CITIES)
	public Response cities() {
		l.debug("cities()::");
		try {
			StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					l.debug("write()::");
					final Timer.Context tc = T_CITIES.time();
					try {
						identifialbes.identifiables(output, Cities.PROTOTYPE);//TODO
					} catch (Exception e) {
						l.error("write():: Unspecific Error occured!", e);
						throw new WebApplicationException(e);
					} finally {
						tc.stop();
					}
				}
			};
			return Response.ok(stream).build();
		} catch (Exception e){
			l.error("Unspecific Error occured!", e);
			return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/cities/{city}/lines")
	@Produces("application/json;charset=utf-8")
	public Response lines(@PathParam("city") String city) {
		l.debug("lines():: City Code [%s], Operator [%s]");
		try {
			StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					l.debug("write()::");
					final Timer.Context tc = T_LINES.time();
					try {
						identifialbes.identifiables(output, Lines.PROTOTYPE);//TODO
					} catch (Exception e) {
						l.error("write():: Unspecific Error occured!", e);
						throw new WebApplicationException(e);
					} finally {
						tc.stop();
					}
				}
			};
			return Response.ok(stream).build();
		} catch (Exception e){
			l.error("lines():: Unspecific Error occured!", e);
			return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
